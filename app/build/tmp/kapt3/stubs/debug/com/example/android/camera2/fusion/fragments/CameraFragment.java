package com.example.android.camera2.fusion.fragments;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u00f8\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0013\n\u0002\b\u000b\u0018\u0000 \u0080\u00012\u00020\u0001:\u0004\u0080\u0001\u0081\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J \u0010L\u001a\u00020M2\u0006\u0010N\u001a\u00020O2\u0006\u0010P\u001a\u00020Q2\u0006\u0010R\u001a\u00020%H\u0002J3\u0010S\u001a\u00020K2\u0006\u0010T\u001a\u00020\u00102\f\u0010U\u001a\b\u0012\u0004\u0012\u00020W0V2\n\b\u0002\u0010X\u001a\u0004\u0018\u00010\u0012H\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010YJ\b\u0010Z\u001a\u00020[H\u0003J\u0010\u0010\\\u001a\u0002042\u0006\u0010]\u001a\u000204H\u0002J$\u0010^\u001a\u00020_2\u0006\u0010`\u001a\u00020a2\b\u0010b\u001a\u0004\u0018\u00010c2\b\u0010d\u001a\u0004\u0018\u00010eH\u0016J\b\u0010f\u001a\u00020MH\u0016J\b\u0010g\u001a\u00020MH\u0016J\b\u0010h\u001a\u00020MH\u0016J\b\u0010i\u001a\u00020MH\u0016J\u001a\u0010j\u001a\u00020M2\u0006\u0010k\u001a\u00020_2\b\u0010d\u001a\u0004\u0018\u00010eH\u0017J-\u0010l\u001a\u00020\u00102\u0006\u0010m\u001a\u00020\u00162\u0006\u0010\u0013\u001a\u00020\u00142\n\b\u0002\u0010X\u001a\u0004\u0018\u00010\u0012H\u0083@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010nJ/\u0010o\u001a\u0004\u0018\u00010p2\u0006\u0010P\u001a\u00020q2\b\b\u0002\u0010r\u001a\u0002002\b\b\u0002\u0010s\u001a\u000200H\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010tJ\u0018\u0010u\u001a\u00020M2\u0006\u0010v\u001a\u00020w2\u0006\u0010x\u001a\u00020wH\u0002J\u0010\u0010y\u001a\u00020M2\u0006\u0010z\u001a\u000200H\u0002J\u001f\u0010{\u001a\b\u0012\u0004\u0012\u00020q0>2\u0006\u0010|\u001a\u00020%H\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010}J\u0011\u0010~\u001a\u00020qH\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u007fR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\t\u001a\u00020\n8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\r\u0010\u000e\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\u000f\u001a\u00020\u0010X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0015\u001a\u00020\u00168BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0019\u0010\u000e\u001a\u0004\b\u0017\u0010\u0018R\u000e\u0010\u001a\u001a\u00020\u001bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u001c\u001a\u00020\u001d8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b \u0010\u000e\u001a\u0004\b\u001e\u0010\u001fR\u0014\u0010!\u001a\u00020\b8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\"\u0010#R\u000e\u0010$\u001a\u00020%X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020\'X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020\u001bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020+X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020%X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010-\u001a\u0004\u0018\u00010.X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010/\u001a\u000200X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u00101\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0004\n\u0002\u00102R\u000e\u00103\u001a\u000204X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u00105\u001a\u0004\u0018\u00010%X\u0082\u000e\u00a2\u0006\u0004\n\u0002\u00106R\u0012\u00107\u001a\u0004\u0018\u00010%X\u0082\u000e\u00a2\u0006\u0004\n\u0002\u00106R\u000e\u00108\u001a\u000204X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u00109\u001a\u00060:R\u00020\u0000X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010;\u001a\u00020<X\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010=\u001a\b\u0012\u0004\u0012\u00020?0>X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010@\u001a\u00020%X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010A\u001a\u00020%X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010B\u001a\u00020%X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010C\u001a\u00020DX\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010E\u001a\u00020F8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\bI\u0010\u000e\u001a\u0004\bG\u0010HR\u000e\u0010J\u001a\u00020KX\u0082.\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0082\u0001"}, d2 = {"Lcom/example/android/camera2/fusion/fragments/CameraFragment;", "Landroidx/fragment/app/Fragment;", "()V", "MICRO_SECOND", "", "MILLI_SECOND", "ONE_SECOND", "_fragmentCameraBinding", "Lcom/example/android/camera2/fusion/databinding/FragmentCameraBinding;", "animationTask", "Ljava/lang/Runnable;", "getAnimationTask", "()Ljava/lang/Runnable;", "animationTask$delegate", "Lkotlin/Lazy;", "camera", "Landroid/hardware/camera2/CameraDevice;", "cameraHandler", "Landroid/os/Handler;", "cameraId", "", "cameraManager", "Landroid/hardware/camera2/CameraManager;", "getCameraManager", "()Landroid/hardware/camera2/CameraManager;", "cameraManager$delegate", "cameraThread", "Landroid/os/HandlerThread;", "characteristics", "Landroid/hardware/camera2/CameraCharacteristics;", "getCharacteristics", "()Landroid/hardware/camera2/CameraCharacteristics;", "characteristics$delegate", "fragmentCameraBinding", "getFragmentCameraBinding", "()Lcom/example/android/camera2/fusion/databinding/FragmentCameraBinding;", "imageFormat", "", "imageReader", "Landroid/media/ImageReader;", "imageReaderHandler", "imageReaderThread", "lightReading", "", "mBoostDiv", "mCaptureCallback", "Landroid/hardware/camera2/CameraCaptureSession$CaptureCallback;", "mChangeBoost", "", "mExposureTime", "Ljava/lang/Long;", "mExposureTimeSetting", "", "mISOBoost", "Ljava/lang/Integer;", "mISOValue", "mISOWeight", "mLightListener", "Lcom/example/android/camera2/fusion/fragments/CameraFragment$LightListener;", "mMultiExposureBuilder", "Landroid/hardware/camera2/CaptureRequest$Builder;", "mMultiExposureRequests", "", "Landroid/hardware/camera2/CaptureRequest;", "mObjectType", "mPanAmplitude", "mTrial", "relativeOrientation", "Lcom/example/android/camera/utils/OrientationLiveData;", "sensorManager", "Landroid/hardware/SensorManager;", "getSensorManager", "()Landroid/hardware/SensorManager;", "sensorManager$delegate", "session", "Landroid/hardware/camera2/CameraCaptureSession;", "addCaptureResultToExif", "", "exif", "Landroidx/exifinterface/media/ExifInterface;", "result", "Landroid/hardware/camera2/CaptureResult;", "orientation", "createCaptureSession", "device", "targets", "", "Landroid/view/Surface;", "handler", "(Landroid/hardware/camera2/CameraDevice;Ljava/util/List;Landroid/os/Handler;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "initializeCamera", "Lkotlinx/coroutines/Job;", "log2", "value", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onDestroyView", "onResume", "onStop", "onViewCreated", "view", "openCamera", "manager", "(Landroid/hardware/camera2/CameraManager;Ljava/lang/String;Landroid/os/Handler;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveResult", "Ljava/io/FileDescriptor;", "Lcom/example/android/camera2/fusion/fragments/CameraFragment$Companion$CombinedCaptureResult;", "isInput", "isAuto", "(Lcom/example/android/camera2/fusion/fragments/CameraFragment$Companion$CombinedCaptureResult;ZZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setExposureBurst", "mTime", "", "mISOMultiplier", "setExposureShort", "m100ms", "takeBurstShot", "mCaptureNumber", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "takePhoto", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "LightListener", "app_debug"})
public final class CameraFragment extends androidx.fragment.app.Fragment {
    private final long MICRO_SECOND = 1000L;
    private final long MILLI_SECOND = 0L;
    private final long ONE_SECOND = 0L;
    
    /**
     * Android ViewBinding
     */
    private com.example.android.camera2.fusion.databinding.FragmentCameraBinding _fragmentCameraBinding;
    
    /**
     * Detects, characterizes, and connects to a CameraDevice (used for all camera operations)
     */
    private final kotlin.Lazy cameraManager$delegate = null;
    
    /**
     * [CameraCharacteristics] corresponding to the provided Camera ID
     */
    private final kotlin.Lazy characteristics$delegate = null;
    
    /**
     * Readers used as buffers for camera still shots
     */
    private android.media.ImageReader imageReader;
    
    /**
     * [HandlerThread] where all camera operations run
     */
    private final android.os.HandlerThread cameraThread = null;
    
    /**
     * [Handler] corresponding to [cameraThread]
     */
    private final android.os.Handler cameraHandler = null;
    
    /**
     * Performs recording animation of flashing screen
     */
    private final kotlin.Lazy animationTask$delegate = null;
    
    /**
     * [HandlerThread] where all buffer reading operations run
     */
    private final android.os.HandlerThread imageReaderThread = null;
    
    /**
     * [Handler] corresponding to [imageReaderThread]
     */
    private final android.os.Handler imageReaderHandler = null;
    
    /**
     * The [CameraDevice] that will be opened in this fragment
     */
    private android.hardware.camera2.CameraDevice camera;
    
    /**
     * Internal reference to the ongoing [CameraCaptureSession] configured with our parameters
     */
    private android.hardware.camera2.CameraCaptureSession session;
    
    /**
     * Live data listener for changes in the device orientation relative to the camera
     */
    private com.example.android.camera.utils.OrientationLiveData relativeOrientation;
    private int imageFormat = android.graphics.ImageFormat.JPEG;
    private java.lang.String cameraId = "0";
    private final kotlin.Lazy sensorManager$delegate = null;
    private float lightReading = 0.0F;
    private com.example.android.camera2.fusion.fragments.CameraFragment.LightListener mLightListener;
    private java.util.List<android.hardware.camera2.CaptureRequest> mMultiExposureRequests;
    private android.hardware.camera2.CaptureRequest.Builder mMultiExposureBuilder;
    private java.lang.Long mExposureTime = 0L;
    private java.lang.Integer mISOValue = 0;
    private java.lang.Integer mISOBoost = 2;
    private int mObjectType = 1;
    private int mBoostDiv = 1;
    private boolean mChangeBoost = false;
    private double mExposureTimeSetting = 1.0;
    private double mISOWeight = 1.0;
    private int mPanAmplitude = 1;
    private int mTrial = 1;
    private android.hardware.camera2.CameraCaptureSession.CaptureCallback mCaptureCallback;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.android.camera2.fusion.fragments.CameraFragment.Companion Companion = null;
    private static final java.lang.String TAG = null;
    
    /**
     * Maximum number of images that will be held in the reader's buffer
     */
    private static final int IMAGE_BUFFER_SIZE = 9;
    
    /**
     * Maximum time allowed to wait for the result of an image capture
     */
    private static final long IMAGE_CAPTURE_TIMEOUT_MILLIS = 5000L;
    
    public CameraFragment() {
        super();
    }
    
    private final com.example.android.camera2.fusion.databinding.FragmentCameraBinding getFragmentCameraBinding() {
        return null;
    }
    
    /**
     * Detects, characterizes, and connects to a CameraDevice (used for all camera operations)
     */
    private final android.hardware.camera2.CameraManager getCameraManager() {
        return null;
    }
    
    /**
     * [CameraCharacteristics] corresponding to the provided Camera ID
     */
    private final android.hardware.camera2.CameraCharacteristics getCharacteristics() {
        return null;
    }
    
    /**
     * Performs recording animation of flashing screen
     */
    private final java.lang.Runnable getAnimationTask() {
        return null;
    }
    
    private final android.hardware.SensorManager getSensorManager() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @android.annotation.SuppressLint(value = {"MissingPermission"})
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    /**
     * Begin all camera operations in a coroutine in the main thread. This function:
     * - Opens the camera
     * - Configures the camera session
     * - Starts the preview by dispatching a repeating capture request
     * - Sets up the still image capture listeners
     */
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    private final kotlinx.coroutines.Job initializeCamera() {
        return null;
    }
    
    /**
     * Opens the camera and returns the opened device (as the result of the suspend coroutine)
     */
    @android.annotation.SuppressLint(value = {"MissingPermission"})
    private final java.lang.Object openCamera(android.hardware.camera2.CameraManager manager, java.lang.String cameraId, android.os.Handler handler, kotlin.coroutines.Continuation<? super android.hardware.camera2.CameraDevice> continuation) {
        return null;
    }
    
    /**
     * Starts a [CameraCaptureSession] and returns the configured session (as the result of the
     * suspend coroutine
     */
    private final java.lang.Object createCaptureSession(android.hardware.camera2.CameraDevice device, java.util.List<? extends android.view.Surface> targets, android.os.Handler handler, kotlin.coroutines.Continuation<? super android.hardware.camera2.CameraCaptureSession> continuation) {
        return null;
    }
    
    private final void setExposureBurst(double[] mTime, double[] mISOMultiplier) {
    }
    
    private final void setExposureShort(boolean m100ms) {
    }
    
    private final java.lang.Object takeBurstShot(int mCaptureNumber, kotlin.coroutines.Continuation<? super java.util.List<com.example.android.camera2.fusion.fragments.CameraFragment.Companion.CombinedCaptureResult>> continuation) {
        return null;
    }
    
    private final void addCaptureResultToExif(androidx.exifinterface.media.ExifInterface exif, android.hardware.camera2.CaptureResult result, int orientation) {
    }
    
    private final double log2(double value) {
        return 0.0;
    }
    
    /**
     * Helper function used to capture a still image using the [CameraDevice.TEMPLATE_STILL_CAPTURE]
     * template. It performs synchronization between the [CaptureResult] and the [Image] resulting
     * from the single capture, and outputs a [CombinedCaptureResult] object.
     */
    private final java.lang.Object takePhoto(kotlin.coroutines.Continuation<? super com.example.android.camera2.fusion.fragments.CameraFragment.Companion.CombinedCaptureResult> continuation) {
        return null;
    }
    
    /**
     * Helper function used to save a [CombinedCaptureResult] into a [File]
     */
    private final java.lang.Object saveResult(com.example.android.camera2.fusion.fragments.CameraFragment.Companion.CombinedCaptureResult result, boolean isInput, boolean isAuto, kotlin.coroutines.Continuation<? super java.io.FileDescriptor> continuation) {
        return null;
    }
    
    @java.lang.Override()
    public void onStop() {
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
    
    @java.lang.Override()
    public void onResume() {
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0082\u0004\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001a\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0010\u0010\t\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u000bH\u0016\u00a8\u0006\f"}, d2 = {"Lcom/example/android/camera2/fusion/fragments/CameraFragment$LightListener;", "Landroid/hardware/SensorEventListener;", "(Lcom/example/android/camera2/fusion/fragments/CameraFragment;)V", "onAccuracyChanged", "", "p0", "Landroid/hardware/Sensor;", "p1", "", "onSensorChanged", "event", "Landroid/hardware/SensorEvent;", "app_debug"})
    final class LightListener implements android.hardware.SensorEventListener {
        
        public LightListener() {
            super();
        }
        
        @java.lang.Override()
        public void onSensorChanged(@org.jetbrains.annotations.NotNull()
        android.hardware.SensorEvent event) {
        }
        
        @java.lang.Override()
        public void onAccuracyChanged(@org.jetbrains.annotations.Nullable()
        android.hardware.Sensor p0, int p1) {
        }
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001:\u0001\tB\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/example/android/camera2/fusion/fragments/CameraFragment$Companion;", "", "()V", "IMAGE_BUFFER_SIZE", "", "IMAGE_CAPTURE_TIMEOUT_MILLIS", "", "TAG", "", "CombinedCaptureResult", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        /**
         * Helper data class used to hold capture metadata with their associated image
         */
        @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B-\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\u0007\u0012\u0006\u0010\t\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\nJ\b\u0010\u0013\u001a\u00020\u0014H\u0016J\t\u0010\u0015\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0016\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0017\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u0018\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\u0007H\u00c6\u0003J;\u0010\u001a\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00072\b\b\u0002\u0010\t\u001a\u00020\u0007H\u00c6\u0001J\u0013\u0010\u001b\u001a\u00020\u001c2\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u00d6\u0003J\t\u0010\u001f\u001a\u00020\u0007H\u00d6\u0001J\t\u0010 \u001a\u00020!H\u00d6\u0001R\u0011\u0010\t\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\b\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\f\u00a8\u0006\""}, d2 = {"Lcom/example/android/camera2/fusion/fragments/CameraFragment$Companion$CombinedCaptureResult;", "Ljava/io/Closeable;", "image", "Landroid/media/Image;", "metadata", "Landroid/hardware/camera2/CaptureResult;", "orientation", "", "format", "count", "(Landroid/media/Image;Landroid/hardware/camera2/CaptureResult;III)V", "getCount", "()I", "getFormat", "getImage", "()Landroid/media/Image;", "getMetadata", "()Landroid/hardware/camera2/CaptureResult;", "getOrientation", "close", "", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "", "other", "", "hashCode", "toString", "", "app_debug"})
        public static final class CombinedCaptureResult implements java.io.Closeable {
            @org.jetbrains.annotations.NotNull()
            private final android.media.Image image = null;
            @org.jetbrains.annotations.NotNull()
            private final android.hardware.camera2.CaptureResult metadata = null;
            private final int orientation = 0;
            private final int format = 0;
            private final int count = 0;
            
            /**
             * Helper data class used to hold capture metadata with their associated image
             */
            @org.jetbrains.annotations.NotNull()
            public final com.example.android.camera2.fusion.fragments.CameraFragment.Companion.CombinedCaptureResult copy(@org.jetbrains.annotations.NotNull()
            android.media.Image image, @org.jetbrains.annotations.NotNull()
            android.hardware.camera2.CaptureResult metadata, int orientation, int format, int count) {
                return null;
            }
            
            /**
             * Helper data class used to hold capture metadata with their associated image
             */
            @java.lang.Override()
            public boolean equals(@org.jetbrains.annotations.Nullable()
            java.lang.Object other) {
                return false;
            }
            
            /**
             * Helper data class used to hold capture metadata with their associated image
             */
            @java.lang.Override()
            public int hashCode() {
                return 0;
            }
            
            /**
             * Helper data class used to hold capture metadata with their associated image
             */
            @org.jetbrains.annotations.NotNull()
            @java.lang.Override()
            public java.lang.String toString() {
                return null;
            }
            
            public CombinedCaptureResult(@org.jetbrains.annotations.NotNull()
            android.media.Image image, @org.jetbrains.annotations.NotNull()
            android.hardware.camera2.CaptureResult metadata, int orientation, int format, int count) {
                super();
            }
            
            @org.jetbrains.annotations.NotNull()
            public final android.media.Image component1() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final android.media.Image getImage() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final android.hardware.camera2.CaptureResult component2() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final android.hardware.camera2.CaptureResult getMetadata() {
                return null;
            }
            
            public final int component3() {
                return 0;
            }
            
            public final int getOrientation() {
                return 0;
            }
            
            public final int component4() {
                return 0;
            }
            
            public final int getFormat() {
                return 0;
            }
            
            public final int component5() {
                return 0;
            }
            
            public final int getCount() {
                return 0;
            }
            
            @java.lang.Override()
            public void close() {
            }
        }
    }
}