/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.camera2.fusion.fragments

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.ImageFormat
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.camera2.*
import android.hardware.camera2.CameraCaptureSession.CaptureCallback
import android.hardware.camera2.CaptureResult.*
import android.media.Image
import android.media.ImageReader
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import android.view.*
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toDrawable
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.android.camera.utils.OrientationLiveData
import com.example.android.camera.utils.computeExifOrientation
import com.example.android.camera.utils.getPreviewOutputSize
import com.example.android.camera2.fusion.CameraActivity
import com.example.android.camera2.fusion.R
import com.example.android.camera2.fusion.databinding.FragmentCameraBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.TimeoutException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.math.roundToInt
import kotlin.math.roundToLong


class CameraFragment : Fragment() {
    private val MICRO_SECOND: Long = 1000
    private val MILLI_SECOND = MICRO_SECOND * 1000
    private val ONE_SECOND = MILLI_SECOND * 1000
    /** Android ViewBinding */
    private var _fragmentCameraBinding: FragmentCameraBinding? = null

    private val fragmentCameraBinding get() = _fragmentCameraBinding!!

    /** Detects, characterizes, and connects to a CameraDevice (used for all camera operations) */
    private val cameraManager: CameraManager by lazy {
        val context = requireContext().applicationContext
        context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }

    /** [CameraCharacteristics] corresponding to the provided Camera ID */
    private val characteristics: CameraCharacteristics by lazy {
        cameraManager.getCameraCharacteristics(cameraId)
    }

    /** Readers used as buffers for camera still shots */
    private lateinit var imageReader: ImageReader

    /** [HandlerThread] where all camera operations run */
    private val cameraThread = HandlerThread("CameraThread").apply { start() }

    /** [Handler] corresponding to [cameraThread] */
    private val cameraHandler = Handler(cameraThread.looper)

    /** Performs recording animation of flashing screen */
    private val animationTask: Runnable by lazy {
        Runnable {
            // Flash white animation
            fragmentCameraBinding.overlay.background = Color.argb(150, 255, 255, 255).toDrawable()
            // Wait for ANIMATION_FAST_MILLIS
            fragmentCameraBinding.overlay.postDelayed({
                // Remove white flash animation
                fragmentCameraBinding.overlay.background = null
            }, CameraActivity.ANIMATION_FAST_MILLIS)
        }
    }

    /** [HandlerThread] where all buffer reading operations run */
    private val imageReaderThread = HandlerThread("imageReaderThread").apply { start() }

    /** [Handler] corresponding to [imageReaderThread] */
    private val imageReaderHandler = Handler(imageReaderThread.looper)

    /** The [CameraDevice] that will be opened in this fragment */
    private lateinit var camera: CameraDevice

    /** Internal reference to the ongoing [CameraCaptureSession] configured with our parameters */
    private lateinit var session: CameraCaptureSession

    /** Live data listener for changes in the device orientation relative to the camera */
    private lateinit var relativeOrientation: OrientationLiveData

    private var imageFormat = ImageFormat.JPEG
    private var cameraId: String = "0"

    private val sensorManager by lazy {requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    private var lightReading: Float = 0F

    private var mLightListener = LightListener()

    // Capture request List for setRepeatingBurst
    private var mMultiExposureRequests = mutableListOf<CaptureRequest>()
    private lateinit var mMultiExposureBuilder: CaptureRequest.Builder

    private var mExposureTime: Long? = 0L
    private var mISOValue: Int? = 0
    private var mISOBoost: Int? = 2

    private var mObjectType: Int = 1
    private var mBoostDiv: Int = 1
    private var mChangeBoost: Boolean = false

    private var mExposureTimeSetting: Double = 1.0
    private var mISOWeight: Double = 1.0
    private var mPanAmplitude: Int = 1

    private var mTrial: Int = 1



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentCameraBinding = FragmentCameraBinding.inflate(inflater, container, false)
        return fragmentCameraBinding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentCameraBinding.captureButton?.setOnApplyWindowInsetsListener { v, insets ->
            v.translationX = (-insets.systemWindowInsetRight).toFloat()
            v.translationY = (-insets.systemWindowInsetBottom).toFloat()
            insets.consumeSystemWindowInsets()
        }

        fragmentCameraBinding.viewFinder.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceDestroyed(holder: SurfaceHolder) = Unit

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int) = Unit

            @RequiresApi(Build.VERSION_CODES.O)
            override fun surfaceCreated(holder: SurfaceHolder) {
                // Selects appropriate preview size and configures view finder
                val previewSize = getPreviewOutputSize(
                    fragmentCameraBinding.viewFinder.display,
                    characteristics,
                    SurfaceHolder::class.java
                )
                Log.d(TAG, "View finder size: ${fragmentCameraBinding.viewFinder.width} x ${fragmentCameraBinding.viewFinder.height}")
                Log.d(TAG, "Selected preview size: $previewSize")
                fragmentCameraBinding.viewFinder.setAspectRatio(
                    previewSize.width,
                    previewSize.height
                )

                // To ensure that size is set, initialize camera in the view's thread
                view.post { initializeCamera() }
            }
        })

        // Used to rotate the output media to match device orientation
        relativeOrientation = OrientationLiveData(requireContext(), characteristics).apply {
            observe(viewLifecycleOwner, Observer { orientation ->
                Log.d(TAG, "Orientation changed: $orientation")
            })
        }

        sensorManager.registerListener(mLightListener,
            sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
            SensorManager.SENSOR_DELAY_NORMAL)
    }

    /**
     * Begin all camera operations in a coroutine in the main thread. This function:
     * - Opens the camera
     * - Configures the camera session
     * - Starts the preview by dispatching a repeating capture request
     * - Sets up the still image capture listeners
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun initializeCamera() = lifecycleScope.launch(Dispatchers.Main) {
        // Open the selected camera
        camera = openCamera(cameraManager, cameraId, cameraHandler)

        val size = Size(1920, 1080)

        imageReader = ImageReader.newInstance(
            size.width, size.height, imageFormat, IMAGE_BUFFER_SIZE
        )

        // Creates list of Surfaces where the camera will output frames
        val targets = listOf(fragmentCameraBinding.viewFinder.holder.surface, imageReader.surface)

        // Start a capture session using our open camera and list of Surfaces where frames will go
        session = createCaptureSession(camera, targets, cameraHandler)

        val captureRequest = camera.createCaptureRequest(
            CameraDevice.TEMPLATE_PREVIEW).apply { addTarget(fragmentCameraBinding.viewFinder.holder.surface) }

        mMultiExposureBuilder = camera.createCaptureRequest(
            CameraDevice.TEMPLATE_STILL_CAPTURE).apply { addTarget(imageReader.surface) }

        // This will keep sending the capture request as frequently as possible until the
        // session is torn down or session.stopRepeating() is called
        session.setRepeatingRequest(captureRequest.build(), mCaptureCallback, cameraHandler)

        lateinit var mExposureTimeValues: DoubleArray
        lateinit var mISOMultiplier: DoubleArray

        fragmentCameraBinding.radioGroup2?.setOnCheckedChangeListener { _, i ->
            when(i){
                R.id.radioButtonType1 -> { mObjectType = 1 }
                R.id.radioButtonType2 -> { mObjectType = 2 }
                R.id.radioButtonType3 -> { mObjectType = 3 }
                R.id.radioButtonType4 -> { mObjectType = 4 }
            }
        }

        fragmentCameraBinding.radioGroup1?.setOnCheckedChangeListener { _, i ->
            when(i){
                R.id.ampButton0 -> { mPanAmplitude = 0 }
                R.id.ampButton1 -> { mPanAmplitude = 1 }
                R.id.ampButton2 -> { mPanAmplitude = 2 }
                R.id.ampButton3 -> { mPanAmplitude = 3 }
            }
        }

        fragmentCameraBinding.radioGroup3?.setOnCheckedChangeListener { _, i ->
            when(i){
                R.id.isoButton0 -> { mChangeBoost = false }
                R.id.isoButton1 -> { mChangeBoost = true }
            }
        }

        val isoTable = doubleArrayOf(0.2, 0.4, 0.6, 0.8, 1.0)
        val timeTable = doubleArrayOf(1000000000.0,500000000.0,250000000.0,125000000.0)

        fragmentCameraBinding.cameraCaptureButton?.setOnClickListener {
            it.isEnabled = false

            // Perform I/O heavy operations in a different scope
            lifecycleScope.launch(Dispatchers.IO) {
                delay(60000L)

                // # of ISO
                for (iso in 1..isoTable.count()) {
                    launch() {
                        takePhoto().use { result ->
                            // Save the result to disk
                            val output = saveResult(result, false, true)
                            // If the result is a JPEG file, update EXIF metadata with orientation info
                            val exif = ExifInterface(output!!)
                            addCaptureResultToExif(exif, result.metadata, result.orientation)
                        }
                    }
                    mISOWeight = isoTable[iso-1]
                    launch {
                        // # of trial
                        for (trial in 1..5) {
                            launch {
                                delay(500L)
                                mTrial = trial

                                // for time weight
                                for (index in 1..timeTable.count()) {
                                    for (capture_index in 1..5){
                                        mISOMultiplier = DoubleArray(capture_index) { mISOWeight }

                                        // TODO(cykwak): Add camera capture with short exposure time
                                        if (capture_index == 1){
                                            setExposureShort(true)
                                            launch {
                                                val burstShotList = takeBurstShot(4)
                                                for (result in burstShotList) {
                                                    val output = saveResult(result, true)
                                                    val exif = ExifInterface(output!!)
                                                    addCaptureResultToExif(
                                                        exif,
                                                        result.metadata,
                                                        result.orientation
                                                    )
                                                }
                                                delay(1000L)
                                            }.join()

                                            setExposureShort(false)
                                            launch {
                                                val burstShotList = takeBurstShot(4)
                                                for (result in burstShotList) {
                                                    val output = saveResult(result, true)
                                                    val exif = ExifInterface(output!!)
                                                    addCaptureResultToExif(
                                                        exif,
                                                        result.metadata,
                                                        result.orientation
                                                    )
                                                }
                                                delay(1000L)
                                            }.join()
                                        }

                                        launch {
                                            mExposureTimeSetting = timeTable[index-1]/capture_index
                                            mExposureTimeValues = DoubleArray(capture_index) { mExposureTimeSetting }
                                            setExposureBurst(mExposureTimeValues, mISOMultiplier)
                                            delay(1000L)
                                        }.join()

                                        launch {
                                            val burstShotList = takeBurstShot(capture_index)
                                            for (result in burstShotList) {
                                                val output = saveResult(result)
                                                val exif = ExifInterface(output!!)
                                                addCaptureResultToExif(exif, result.metadata, result.orientation)
                                            }
                                        }.join()
                                    }
                                }
                            }.join()
                        }
                    }.join()
                }
            }

            it.post { it.isEnabled = true }
        }
    }

    /** Opens the camera and returns the opened device (as the result of the suspend coroutine) */
    @SuppressLint("MissingPermission")
    private suspend fun openCamera(
        manager: CameraManager,
        cameraId: String,
        handler: Handler? = null
    ): CameraDevice = suspendCancellableCoroutine { cont ->
        manager.openCamera(cameraId, object : CameraDevice.StateCallback() {
            override fun onOpened(device: CameraDevice) = cont.resume(device)

            override fun onDisconnected(device: CameraDevice) {
                Log.w(TAG, "Camera $cameraId has been disconnected")
                requireActivity().finish()
            }

            override fun onError(device: CameraDevice, error: Int) {
                val msg = when (error) {
                    ERROR_CAMERA_DEVICE -> "Fatal (device)"
                    ERROR_CAMERA_DISABLED -> "Device policy"
                    ERROR_CAMERA_IN_USE -> "Camera in use"
                    ERROR_CAMERA_SERVICE -> "Fatal (service)"
                    ERROR_MAX_CAMERAS_IN_USE -> "Maximum cameras in use"
                    else -> "Unknown"
                }
                val exc = RuntimeException("Camera $cameraId error: ($error) $msg")
                Log.e(TAG, exc.message, exc)
                if (cont.isActive) cont.resumeWithException(exc)
            }
        }, handler)
    }

    /**
     * Starts a [CameraCaptureSession] and returns the configured session (as the result of the
     * suspend coroutine
     */
    private suspend fun createCaptureSession(
        device: CameraDevice,
        targets: List<Surface>,
        handler: Handler? = null
    ): CameraCaptureSession = suspendCoroutine { cont ->

        // Create a capture session using the predefined targets; this also involves defining the
        // session state callback to be notified of when the session is ready
        device.createCaptureSession(targets, object : CameraCaptureSession.StateCallback() {

            override fun onConfigured(session: CameraCaptureSession) = cont.resume(session)

            override fun onConfigureFailed(session: CameraCaptureSession) {
                val exc = RuntimeException("Camera ${device.id} session configuration failed")
                Log.e(TAG, exc.message, exc)
                cont.resumeWithException(exc)
            }
        }, handler)
    }

    private fun setExposureBurst(mTime: DoubleArray, mISOMultiplier: DoubleArray) {
        mMultiExposureRequests.clear()
        // turn off auto exposure
        mMultiExposureBuilder.set(CaptureRequest.CONTROL_AWB_MODE, CaptureRequest.CONTROL_AWB_MODE_AUTO)
        mMultiExposureBuilder.set(CaptureRequest.NOISE_REDUCTION_MODE,CaptureRequest.NOISE_REDUCTION_MODE_FAST)
        mMultiExposureBuilder.set(CaptureRequest.EDGE_MODE,CaptureRequest.EDGE_MODE_FAST)
        mMultiExposureBuilder.set(CaptureRequest.SHADING_MODE,CaptureRequest.HOT_PIXEL_MODE_FAST)
        mMultiExposureBuilder.set(CaptureRequest.HOT_PIXEL_MODE,CaptureRequest.HOT_PIXEL_MODE_FAST)
        mMultiExposureBuilder.set(CaptureRequest.CONTROL_AE_MODE,CaptureRequest.CONTROL_AE_MODE_OFF)
        mMultiExposureBuilder.set(CaptureRequest.TONEMAP_MODE,CaptureRequest.TONEMAP_MODE_FAST)
        mMultiExposureBuilder.set(CaptureRequest.SENSOR_FRAME_DURATION, ONE_SECOND / 30)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (mISOBoost!! >= 200) {
                if (!mChangeBoost){
                    mMultiExposureBuilder.set(
                    CaptureRequest.CONTROL_POST_RAW_SENSITIVITY_BOOST,
                    mISOBoost?.div(mBoostDiv)
                )}

            }
            else if (mISOBoost!! >= 100){
                mMultiExposureBuilder.set(CaptureRequest.CONTROL_POST_RAW_SENSITIVITY_BOOST, mISOBoost)
            }
        }

        if (mChangeBoost) {
            for ( mMultiplier in mISOMultiplier.zip(mTime)){
                mMultiExposureBuilder.set(CaptureRequest.SENSOR_SENSITIVITY, mISOValue)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    mMultiExposureBuilder.set(
                        CaptureRequest.CONTROL_POST_RAW_SENSITIVITY_BOOST,
                        mISOBoost?.div(mBoostDiv)?.times(mMultiplier.first)?.roundToInt()
                    )
                }
                mMultiExposureBuilder.set(CaptureRequest.SENSOR_EXPOSURE_TIME, mMultiplier.second.roundToLong())
                mMultiExposureRequests.add(mMultiExposureBuilder.build())
            }
        }else {
            for ( mMultiplier in mISOMultiplier.zip(mTime)){
                mMultiExposureBuilder.set(CaptureRequest.SENSOR_SENSITIVITY, mISOValue?.times(mMultiplier.first)?.roundToInt())
                mMultiExposureBuilder.set(CaptureRequest.SENSOR_EXPOSURE_TIME, mMultiplier.second.roundToLong())
                mMultiExposureRequests.add(mMultiExposureBuilder.build())
            }
        }

    }

    private fun setExposureShort(m100ms: Boolean) {
        mMultiExposureRequests.clear()
        // turn off auto exposure
        mMultiExposureBuilder.set(CaptureRequest.CONTROL_AWB_MODE, CaptureRequest.CONTROL_AWB_MODE_AUTO)
        mMultiExposureBuilder.set(CaptureRequest.NOISE_REDUCTION_MODE,CaptureRequest.NOISE_REDUCTION_MODE_FAST)
        mMultiExposureBuilder.set(CaptureRequest.EDGE_MODE,CaptureRequest.EDGE_MODE_FAST)
        mMultiExposureBuilder.set(CaptureRequest.SHADING_MODE,CaptureRequest.HOT_PIXEL_MODE_FAST)
        mMultiExposureBuilder.set(CaptureRequest.HOT_PIXEL_MODE,CaptureRequest.HOT_PIXEL_MODE_FAST)
        mMultiExposureBuilder.set(CaptureRequest.CONTROL_AE_MODE,CaptureRequest.CONTROL_AE_MODE_OFF)
        mMultiExposureBuilder.set(CaptureRequest.TONEMAP_MODE,CaptureRequest.TONEMAP_MODE_FAST)
        mMultiExposureBuilder.set(CaptureRequest.SENSOR_FRAME_DURATION, ONE_SECOND / 30)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            mMultiExposureBuilder.set(CaptureRequest.CONTROL_POST_RAW_SENSITIVITY_BOOST, 100)
        }

        for ( i in 1..4){
            mMultiExposureBuilder.set(CaptureRequest.SENSOR_SENSITIVITY, 500)
            if (m100ms){
                mMultiExposureBuilder.set(CaptureRequest.SENSOR_EXPOSURE_TIME, 10000000L)
            }
            else{
                mMultiExposureBuilder.set(CaptureRequest.SENSOR_EXPOSURE_TIME, 20000000L)
            }
            mMultiExposureRequests.add(mMultiExposureBuilder.build())
        }

        mMultiExposureBuilder.set(CaptureRequest.CONTROL_AE_MODE,CaptureRequest.CONTROL_AE_MODE_OFF)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mMultiExposureBuilder.set(CaptureRequest.CONTROL_POST_RAW_SENSITIVITY_BOOST, 100)
        }

        for ( i in 1..4){
            mMultiExposureBuilder.set(CaptureRequest.SENSOR_SENSITIVITY, 500)
            if (m100ms){
                mMultiExposureBuilder.set(CaptureRequest.SENSOR_EXPOSURE_TIME, 10000000L)
            }
            else{
                mMultiExposureBuilder.set(CaptureRequest.SENSOR_EXPOSURE_TIME, 20000000L)
            }
            mMultiExposureRequests.add(mMultiExposureBuilder.build())
        }
    }




    private suspend fun takeBurstShot(mCaptureNumber: Int):
            MutableList<CombinedCaptureResult> = suspendCoroutine { cont ->
        // Flush any images left in the image reader
        @Suppress("ControlFlowWithEmptyBody")
        while (imageReader.acquireNextImage() != null) {
        }

        // Start a new image queue
        val imageQueue = ArrayBlockingQueue<Image>(IMAGE_BUFFER_SIZE)
        imageReader.setOnImageAvailableListener({ reader ->
            val image = reader.acquireNextImage()
            Log.d(TAG, "Image available in queue: ${image.timestamp}")
            imageQueue.add(image)
        }, imageReaderHandler)

        var count = 0
        val burstShotList = mutableListOf<CombinedCaptureResult>()

        session.captureBurst(mMultiExposureRequests, object : CameraCaptureSession.CaptureCallback() {
            override fun onCaptureStarted(
                session: CameraCaptureSession,
                request: CaptureRequest,
                timestamp: Long,
                frameNumber: Long) {
                super.onCaptureStarted(session, request, timestamp, frameNumber)
                fragmentCameraBinding.viewFinder.post(animationTask)
            }

            override fun onCaptureCompleted(
                session: CameraCaptureSession,
                request: CaptureRequest,
                result: TotalCaptureResult) {
                super.onCaptureCompleted(session, request, result)

                val resultTimestamp = result.get(CaptureResult.SENSOR_TIMESTAMP)
                Log.d(TAG, "Capture result received: $resultTimestamp")

                // Loop in the coroutine's context until an image with matching timestamp comes
                // We need to launch the coroutine context again because the callback is done in
                // the handler provided to the `capture` method, not in our coroutine context
                @Suppress("BlockingMethodInNonBlockingContext")
                lifecycleScope.launch(cont.context) {
                    while (true) {
                        // Dequeue images while timestamps don't match
                        val image = imageQueue.take()
                        // TODO(owahltinez): b/142011420
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
                            image.format != ImageFormat.DEPTH_JPEG &&
                            image.timestamp != resultTimestamp) continue

                        count++

                        // Compute EXIF orientation metadata
                        val rotation = relativeOrientation.value?: 0
                        val mirrored = characteristics.get(CameraCharacteristics.LENS_FACING) ==
                                CameraCharacteristics.LENS_FACING_FRONT
                        val exifOrientation = computeExifOrientation(rotation, mirrored)

                        // Build the result and resume progress
                        burstShotList.add(
                            CombinedCaptureResult(
                                image, result, exifOrientation, imageReader.imageFormat, count)
                        )
                        if (count == mCaptureNumber){
                            imageReader.setOnImageAvailableListener(null, null)

                            while (imageQueue.size > 0) {
                                imageQueue.take().close()
                            }
                            cont.resume(burstShotList)
                        }

                        break
                    }
                }
            }
        }, cameraHandler)
    }


    private fun addCaptureResultToExif(exif: ExifInterface, result: CaptureResult, orientation: Int){
        exif.setAttribute(ExifInterface.TAG_MAKE, "Google")
        exif.setAttribute(ExifInterface.TAG_MODEL, "Pixel 4")
        exif.setAttribute(ExifInterface.TAG_ORIENTATION, orientation.toString())

        // Exposure time
        val NS_TO_S = 1000000000.0

        val exposureTimeNs = result.get(SENSOR_EXPOSURE_TIME)
        val exposureTime = exposureTimeNs?.toDouble()?.div(NS_TO_S)
        exif.setAttribute(ExifInterface.TAG_EXPOSURE_TIME, "$exposureTime")

        val focalLength = result.get(CaptureResult.LENS_FOCAL_LENGTH)
        exif.setAttribute(ExifInterface.TAG_FOCAL_LENGTH,"$focalLength")
        val fNumber = result.get(LENS_APERTURE)
        exif.setAttribute(ExifInterface.TAG_F_NUMBER,"$fNumber")
        val apertureValue = 2 * log2(fNumber?.toDouble()!!)

        exif.setAttribute(ExifInterface.TAG_APERTURE_VALUE,"$apertureValue")

        exif.setAttribute(ExifInterface.TAG_PHOTOGRAPHIC_SENSITIVITY, "${result.get(SENSOR_SENSITIVITY)}")
        exif.saveAttributes()

    }

    private fun log2(value: Double): Double {
        return Math.log(value) / Math.log(2.0);
    }

    /**
     * Helper function used to capture a still image using the [CameraDevice.TEMPLATE_STILL_CAPTURE]
     * template. It performs synchronization between the [CaptureResult] and the [Image] resulting
     * from the single capture, and outputs a [CombinedCaptureResult] object.
     */
    private suspend fun takePhoto():
            CombinedCaptureResult = suspendCoroutine { cont ->
        // Flush any images left in the image reader
        @Suppress("ControlFlowWithEmptyBody")
        while (imageReader.acquireNextImage() != null) {
        }

        // Start a new image queue
        val imageQueue = ArrayBlockingQueue<Image>(IMAGE_BUFFER_SIZE)
        imageReader.setOnImageAvailableListener({ reader ->
            val image = reader.acquireNextImage()
//            Log.d(TAG, "Image available in queue: ${image.timestamp}")
            imageQueue.add(image)
        }, imageReaderHandler)

        val captureRequest = session.device.createCaptureRequest(
            CameraDevice.TEMPLATE_STILL_CAPTURE).apply { addTarget(imageReader.surface) }
        session.capture(captureRequest.build(), object : CameraCaptureSession.CaptureCallback() {

            override fun onCaptureStarted(
                session: CameraCaptureSession,
                request: CaptureRequest,
                timestamp: Long,
                frameNumber: Long) {
                super.onCaptureStarted(session, request, timestamp, frameNumber)
                fragmentCameraBinding.viewFinder.post(animationTask)
            }

            override fun onCaptureCompleted(
                session: CameraCaptureSession,
                request: CaptureRequest,
                result: TotalCaptureResult) {
                super.onCaptureCompleted(session, request, result)
                val resultTimestamp = result.get(CaptureResult.SENSOR_TIMESTAMP)
//                Log.d(TAG, "Capture result received: $resultTimestamp")

                // Set a timeout in case image captured is dropped from the pipeline
                val exc = TimeoutException("Image dequeuing took too long")
                val timeoutRunnable = Runnable { cont.resumeWithException(exc) }
                imageReaderHandler.postDelayed(timeoutRunnable, IMAGE_CAPTURE_TIMEOUT_MILLIS)

                // Loop in the coroutine's context until an image with matching timestamp comes
                // We need to launch the coroutine context again because the callback is done in
                //  the handler provided to the `capture` method, not in our coroutine context
                @Suppress("BlockingMethodInNonBlockingContext")
                lifecycleScope.launch(cont.context) {
                    while (true) {
                        // Dequeue images while timestamps don't match
                        val image = imageQueue.take()
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
                            image.format != ImageFormat.DEPTH_JPEG &&
                            image.timestamp != resultTimestamp) continue
//                        Log.d(TAG, "Matching image dequeued: ${image.timestamp}")

                        // Unset the image reader listener
                        imageReaderHandler.removeCallbacks(timeoutRunnable)
                        imageReader.setOnImageAvailableListener(null, null)

                        // Clear the queue of images, if there are left
                        while (imageQueue.size > 0) {
                            imageQueue.take().close()
                        }

                        // Compute EXIF orientation metadata
                        val rotation = relativeOrientation.value?: 0
                        val mirrored = characteristics.get(CameraCharacteristics.LENS_FACING) ==
                                CameraCharacteristics.LENS_FACING_FRONT
                        val exifOrientation = computeExifOrientation(rotation, mirrored)

                        // Build the result and resume progress
                        cont.resume(CombinedCaptureResult(image, result, exifOrientation, imageReader.imageFormat, 1))
                    }
                }
            }
        }, cameraHandler)
    }

    /** Helper function used to save a [CombinedCaptureResult] into a [File] */
    private suspend fun saveResult(result: CombinedCaptureResult, isInput: Boolean = false, isAuto: Boolean = false): FileDescriptor? = suspendCoroutine { cont ->
        when (result.format) {
            // When the format is JPEG or DEPTH JPEG we can simply save the bytes as-is
            ImageFormat.JPEG, ImageFormat.DEPTH_JPEG -> {
                val buffer = result.image.planes[0].buffer
                val bytes = ByteArray(buffer.remaining()).apply { buffer.get(this) }
                result.image.close()
                try {
                    val sdf = SimpleDateFormat("MMdd_HH_mm_ss", Locale.US)
                    val NS_TO_S = 1000000000.0

                    val exposureTimeNs = result.metadata.get(SENSOR_EXPOSURE_TIME)
                    val df2 = DecimalFormat("##0.000")
                    val df = DecimalFormat("##0.00")
                    val mExp = exposureTimeNs?.toDouble()?.div(NS_TO_S)
                    val ae = mExposureTime?.toDouble()?.div(NS_TO_S)
                    val mISO = result.metadata.get(SENSOR_SENSITIVITY)
                    var mBoost: Int? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        result.metadata.get(CONTROL_POST_RAW_SENSITIVITY_BOOST)
                    }else{
                        100
                    }

                    val filename = if (isInput) {
                        if (isAuto){
                            "Auto_${sdf.format(Date())}.jpg"
                        }else{
                            "input_${sdf.format(Date())}_B${df.format(lightReading)}_I${mISO}_AE${df2.format(mExp)}_AMP${mPanAmplitude}_TYPE${mObjectType}_${result.count}.jpg"
                        }
                    }else{
                        "${sdf.format(Date())}_B${df.format(lightReading)}_I${mISO}_AE${df2.format(ae)}_BST${mBoost}_ISO${mISOWeight}_AMP${mPanAmplitude}_TYPE${mObjectType}_ET${df2.format(mExp)}_TRIAL${mTrial}_${result.count}.jpg"
                    }

                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    val values = ContentValues()

                    values.put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                    values.put(MediaStore.Images.Media.MIME_TYPE,"image/jpg")

                    var uri: Uri? = requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                    requireContext().contentResolver.openOutputStream(uri!!).use{
                        if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 95, it))
                            throw IOException("Failed to save bitmap.")
                    }

                    val pdf = requireContext().contentResolver.openFileDescriptor(uri!!,"w")
                    val output = pdf?.getFileDescriptor()

                    cont.resume(output)
                } catch (exc: IOException) {
                    Log.e(TAG, "Unable to write JPEG image to file", exc)
                    cont.resumeWithException(exc)
                }
            }

            // No other formats are supported by this sample
            else -> {
                val exc = RuntimeException("Unknown image format: ${result.image.format}")
                Log.e(TAG, exc.message, exc)
                cont.resumeWithException(exc)
            }
        }
    }

    private var mCaptureCallback: CaptureCallback? = object : CaptureCallback() {
        @RequiresApi(Build.VERSION_CODES.N)
        override fun onCaptureCompleted(
            session: CameraCaptureSession,
            request: CaptureRequest,
            result: TotalCaptureResult
        ) {
            // Only update UI every so many frames
            // Use an odd number here to ensure both even and odd exposures get an occasional update
            mExposureTime = result.get(SENSOR_EXPOSURE_TIME)
            mISOValue = result.get(SENSOR_SENSITIVITY)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mISOBoost = result.get(CONTROL_POST_RAW_SENSITIVITY_BOOST)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        try {
            camera.close()
        } catch (exc: Throwable) {
            Log.e(TAG, "Error closing camera", exc)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraThread.quitSafely()
        imageReaderThread.quitSafely()
    }

    override fun onDestroyView() {
        _fragmentCameraBinding = null
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(mLightListener,
        sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
        SensorManager.SENSOR_DELAY_NORMAL)
    }

    private inner class LightListener: SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_LIGHT){
                lightReading = event.values[0]
            }
        }

        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        }
    }

    companion object {
        private val TAG = CameraFragment::class.java.simpleName

        /** Maximum number of images that will be held in the reader's buffer */
        private const val IMAGE_BUFFER_SIZE: Int = 9

        /** Maximum time allowed to wait for the result of an image capture */
        private const val IMAGE_CAPTURE_TIMEOUT_MILLIS: Long = 5000

        /** Helper data class used to hold capture metadata with their associated image */
        data class CombinedCaptureResult(
            val image: Image,
            val metadata: CaptureResult,
            val orientation: Int,
            val format: Int,
            val count: Int
        ) : Closeable {
            override fun close() = image.close()
        }

    }
}
