package edess.example.webviewimagecollection;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.SyncStateContract.Constants;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled") 
public class WebViewActivity extends ActionBarActivity {

	private WebView webView;
	 public static final String PREFS_FILE = "myPreferenceFiles";
	 SharedPreferences settings;
	 
	 SharedPreferences sharedpreferences;
	 SharedPreferences.Editor editor; 
	 
	 String email_Of_User;
	 //Button btnRequestGain;   I deleted this button from the xml
	 Spinner spinnerSeeDetails; 
	 ArrayAdapter<CharSequence> adapterForSpinner; 
	 //double mLongitude=0;
	 //double mLatitude=0; 
	 
	 private ValueCallback<Uri[]> mFilePathCallback;
	    private String mCameraPhotoPath;
	    public static final int INPUT_FILE_REQUEST_CODE = 1; 
	    private static final String TAG = WebViewActivity.class.getSimpleName();
	 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);
		
		settings = getSharedPreferences(PREFS_FILE,0);
		email_Of_User= settings.getString("userEmail", "not.email"); 
		//String ua =  "Mozilla/5.0 (Linux; Android 4.4; Nexus 4 Build/KRT16H) AppleWebKit/537.3 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36"; 
		
		sharedpreferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
		
		webView = (WebView) findViewById(R.id.webViewUpload);
		webView.getSettings().setJavaScriptEnabled(true); 
		
		
		webView.getSettings().setBuiltInZoomControls(true); 
		//webView.setWebViewClient(new WebViewClient());
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		//webView.getSettings().setUserAgentString(ua);
		 
		//webView.loadUrl("http://www.google.com/webhp?hl=en&output=html");
		webView.loadUrl("http://160.16.101.96/~cedric-k/calorie/htmlform.html");
		webView.getSettings().setDomStorageEnabled(true);
		webView.addJavascriptInterface(new WebAppInterface(this), "Android");
		/*
		below lines are for enabling photo selection and camera activation on Android version higher than 4.3  
		source : https://github.com/GoogleChrome/chromium-webview-samples/blob/master/input-file-example/app/src/main/java/inputfilesample/android/chrome/google/com/inputfilesample/MainFragment.java
		
		start 
		*/
		
		webView.setWebChromeClient(new WebChromeClient() {
		    public boolean onShowFileChooser(
		            WebView webView, ValueCallback<Uri[]> filePathCallback,
		            WebChromeClient.FileChooserParams fileChooserParams) {

		        // Double check that we don't have any existing callbacks
		        if(mFilePathCallback != null) {
		            mFilePathCallback.onReceiveValue(null);
		        }
		        mFilePathCallback = filePathCallback;

		        // Set up the take picture intent
		        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		        if (takePictureIntent.resolveActivity(getApplication().getPackageManager()) != null) {
		            // Create the File where the photo should go
		            File photoFile = null;
		            try {
		                photoFile = createImageFile();
		                takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
		            } catch (IOException ex) {
		                // Error occurred while creating the File
		                Log.e(TAG, "Unable to create Image File", ex);
		            }

		            // Continue only if the File was successfully created
		            if (photoFile != null) {
		                mCameraPhotoPath = "file:"  +photoFile.getAbsolutePath();
		                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
		                        Uri.fromFile(photoFile));
		            } else {
		                takePictureIntent = null;
		            }
		        }

		        // Set up the intent to get an existing image
		        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
		        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
		        contentSelectionIntent.setType("image/*");

		        // Set up the intents for the Intent chooser
		        Intent[] intentArray;
		        if(takePictureIntent != null) {
		            intentArray = new Intent[]{takePictureIntent};
		        } else {
		            intentArray = new Intent[0];
		        }

		        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
		        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
		        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
		        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

		        startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);

		        return true;
		    }
		});
		
		
		/*
		 * end of code for enabling file selection and camera on android >4.3
		*/
		//webView.setWebChromeClient(new WebChromeClient()); 
		
		
		webView.setWebViewClient(new WebViewClient(){
			
			
			public void onPageFinished(WebView view, String url) {
		        String email=email_Of_User;
		       
				String longit = sharedpreferences.getString("long_ti", "0"); 
				String latit = sharedpreferences.getString("la_ti", "0"); 
				
				System.out.println("longitude est= "+longit);
				System.out.println("latitude est= "+latit);

		        //String pwd="p";
		        
		        // send the user email from the android app to the php/html code to insert in the DB.
		        view.loadUrl("javascript:init('"+email+"')");
		        view.loadUrl("javascript:longitude('"+longit+"')"); // send longitude coord to DB
		        view.loadUrl("javascript:latitude('"+latit+"')"); // send latitude coord to DB

		        //view.loadUrl("javascript:document.getElementById('emailUser').value = '"+email+"'");
		        //view.loadUrl("javascript:document.forms[0].email.value = '"+email+"';");
		        Log.d("email", "passing email to DB");
			} 
		});
		
		initWebView(webView); 
		
		
		// details for spinner sont ici : https://www.youtube.com/watch?v=28jA5-mO8K8
		/*spinnerSeeDetails=(Spinner)findViewById(R.id.spinnerOptions);
		adapterForSpinner= ArrayAdapter.createFromResource(this, R.array.list_of_options, android.R.layout.simple_spinner_item);
		adapterForSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerSeeDetails.setAdapter(adapterForSpinner);
		spinnerSeeDetails.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				//Toast.makeText(getApplicationContext(), parent.getItemAtPosition(position)+" is selected", Toast.LENGTH_LONG).show();
				String strSpinnerValue = spinnerSeeDetails.getSelectedItem().toString();
				
				if(strSpinnerValue.equals("Request rewards"))
				{
					startActivity(new Intent(WebViewActivity.this, DetailsMoneyRequest.class));
				
				}
				else if(strSpinnerValue.equals("Contact developers"))
				{
					startActivity(new Intent(WebViewActivity.this, ContactDeveloppers.class));
				
				}
				else if(strSpinnerValue.equals("My Rewarded images")) 
				{
					startActivity(new Intent(WebViewActivity.this, RewardedImages.class));
				
				}
				
					
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});*/
		
	
}
	
	 /** 
	 * this method is used by the webView.setChromeClient for Android version higher than 4.3
     * More info this method can be found at 
     * http://developer.android.com/training/camera/photobasics.html 
     * 
     * @return 
     * @throws IOException 
     */ 
    private File createImageFile() throws IOException {
        // Create an image file name 
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */ 
                storageDir      /* directory */
        ); 
        return imageFile;
    } 
    
    


	
	
	private final static Object methodInvoke(Object obj, String method, Class<?>[] parameterTypes, Object[] args) {
        try {
            Method m = obj.getClass().getMethod(method, new Class[] { boolean.class });
            m.invoke(obj, args);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @SuppressWarnings("deprecation")
	private void initWebView(WebView webView) {

        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true);
        
       
        
        // settings.setPluginsEnabled(true);
        methodInvoke(settings, "setPluginsEnabled", new Class[] { boolean.class }, new Object[] { true });
        
        settings.setPluginState(PluginState.ON);
        //methodInvoke(settings, "setPluginState", new Class[] { PluginState.class }, new Object[] { PluginState.ON });
        // settings.setPluginsEnabled(true);
        methodInvoke(settings, "setPluginsEnabled", new Class[] { boolean.class }, new Object[] { true });
        // settings.setAllowUniversalAccessFromFileURLs(true);
        methodInvoke(settings, "setAllowUniversalAccessFromFileURLs", new Class[] { boolean.class }, new Object[] { true });
        // settings.setAllowFileAccessFromFileURLs(true);
        methodInvoke(settings, "setAllowFileAccessFromFileURLs", new Class[] { boolean.class }, new Object[] { true });
        
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.clearHistory();
        webView.clearFormData();
        webView.clearCache(true);

        //webView.setWebChromeClient(new MyWebChromeClient());
        // webView.setDownloadListener(downloadListener);
    }

    UploadHandler mUploadHandler;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == Controller.FILE_SELECTED) {
            // Chose a file from the file picker.
            if (mUploadHandler != null) {
                mUploadHandler.onResult(resultCode, intent);
            }
        }
        if(requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
            super.onActivityResult(requestCode, resultCode, intent);
            return; 
        } 
 
 
        Uri[] results = null;
 
 
        // Check that the response is a good one 
        if(resultCode == Activity.RESULT_OK) {
            if(intent == null) {
                // If there is not data, then we may have taken a photo 
                if(mCameraPhotoPath != null) {
                    results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                } 
            } else { 
                String dataString = intent.getDataString();
                if (dataString != null) {
                    results = new Uri[]{Uri.parse(dataString)};
                } 
            } 
        } 
 
 
        mFilePathCallback.onReceiveValue(results);
        mFilePathCallback = null;
        return; 

        //super.onActivityResult(requestCode, resultCode, intent);
    }

    class MyWebChromeClient extends WebChromeClient {
        public MyWebChromeClient() {

        }

        private String getTitleFromUrl(String url) {
            String title = url;
            try {
                URL urlObj = new URL(url);
                String host = urlObj.getHost();
                if (host != null && !host.isEmpty()) {
                    return urlObj.getProtocol() + "://" + host;
                }
                if (url.startsWith("file:")) {
                    String fileName = urlObj.getFile();
                    if (fileName != null && !fileName.isEmpty()) {
                        return fileName;
                    }
                }
            } catch (Exception e) {
                // ignore
            }

            return title;
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            String newTitle = getTitleFromUrl(url);

            new AlertDialog.Builder(WebViewActivity.this).setTitle(newTitle).setMessage(message).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm();
                }
            }).setCancelable(false).create().show();
            return true;
            // return super.onJsAlert(view, url, message, result);
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {

            String newTitle = getTitleFromUrl(url);

            new AlertDialog.Builder(WebViewActivity.this).setTitle(newTitle).setMessage(message).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm();
                }
            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    result.cancel();
                }
            }).setCancelable(false).create().show();
            return true;

            // return super.onJsConfirm(view, url, message, result);
        }

        // Android 2.x
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooser(uploadMsg, "");
        }

        // Android 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            openFileChooser(uploadMsg, "", "filesystem");
        }

        // Android 4.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            mUploadHandler = new UploadHandler(new Controller());
            mUploadHandler.openFileChooser(uploadMsg, acceptType, capture);
        }
    };

    class Controller {
        final static int FILE_SELECTED = 4;

        Activity getActivity() {
            return WebViewActivity.this;
        }
    }


    class UploadHandler {
        /*
         * The Object used to inform the WebView of the file to upload.
         */
        private ValueCallback<Uri> mUploadMessage;
        private String mCameraFilePath;
        private boolean mHandled;
        private boolean mCaughtActivityNotFoundException;
        private Controller mController;
        public UploadHandler(Controller controller) {
            mController = controller;
        }
        String getFilePath() {
            return mCameraFilePath;
        }
        boolean handled() {
            return mHandled;
        }
        void onResult(int resultCode, Intent intent) {
            if (resultCode == Activity.RESULT_CANCELED && mCaughtActivityNotFoundException) {
                // Couldn't resolve an activity, we are going to try again so skip
                // this result.
                mCaughtActivityNotFoundException = false;
                return;
            }
            Uri result = intent == null || resultCode != Activity.RESULT_OK ? null
                    : intent.getData();
            // As we ask the camera to save the result of the user taking
            // a picture, the camera application does not return anything other
            // than RESULT_OK. So we need to check whether the file we expected
            // was written to disk in the in the case that we
            // did not get an intent returned but did get a RESULT_OK. If it was,
            // we assume that this result has came back from the camera.
            if (result == null && intent == null && resultCode == Activity.RESULT_OK) {
                File cameraFile = new File(mCameraFilePath);
                if (cameraFile.exists()) {
                    result = Uri.fromFile(cameraFile);
                    // Broadcast to the media scanner that we have a new photo
                    // so it will be added into the gallery for the user.
                    mController.getActivity().sendBroadcast(
                            new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, result));
                }
            }
            mUploadMessage.onReceiveValue(result);
            mHandled = true;
            mCaughtActivityNotFoundException = false;
        }
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            final String imageMimeType = "image/*";
            final String videoMimeType = "video/*";
            final String audioMimeType = "audio/*";
            final String mediaSourceKey = "capture";
            final String mediaSourceValueCamera = "camera";
            final String mediaSourceValueFileSystem = "filesystem";
            final String mediaSourceValueCamcorder = "camcorder";
            final String mediaSourceValueMicrophone = "microphone";
            // According to the spec, media source can be 'filesystem' or 'camera' or 'camcorder'
            // or 'microphone' and the default value should be 'filesystem'.
            String mediaSource = mediaSourceValueFileSystem;
            if (mUploadMessage != null) {
                // Already a file picker operation in progress.
                return;
            }
            mUploadMessage = uploadMsg;
            // Parse the accept type.
            String params[] = acceptType.split(";");
            String mimeType = params[0];
            if (capture.length() > 0) {
                mediaSource = capture;
            }
            if (capture.equals(mediaSourceValueFileSystem)) {
                // To maintain backwards compatibility with the previous implementation
                // of the media capture API, if the value of the 'capture' attribute is
                // "filesystem", we should examine the accept-type for a MIME type that
                // may specify a different capture value.
                for (String p : params) {
                    String[] keyValue = p.split("=");
                    if (keyValue.length == 2) {
                        // Process key=value parameters.
                        if (mediaSourceKey.equals(keyValue[0])) {
                            mediaSource = keyValue[1];
                        }
                    }
                }
            }
            //Ensure it is not still set from a previous upload.
            mCameraFilePath = null;
            if (mimeType.equals(imageMimeType)) {
                if (mediaSource.equals(mediaSourceValueCamera)) {
                    // Specified 'image/*' and requested the camera, so go ahead and launch the
                    // camera directly.
                    startActivity(createCameraIntent());
                    return;
                } else {
                    // Specified just 'image/*', capture=filesystem, or an invalid capture parameter.
                    // In all these cases we show a traditional picker filetered on accept type
                    // so launch an intent for both the Camera and image/* OPENABLE.
                    Intent chooser = createChooserIntent(createCameraIntent());
                    chooser.putExtra(Intent.EXTRA_INTENT, createOpenableIntent(imageMimeType));
                    startActivity(chooser);
                    return;
                }
            } else if (mimeType.equals(videoMimeType)) {
                if (mediaSource.equals(mediaSourceValueCamcorder)) {
                    // Specified 'video/*' and requested the camcorder, so go ahead and launch the
                    // camcorder directly.
                    startActivity(createCamcorderIntent());
                    return;
               } else {
                    // Specified just 'video/*', capture=filesystem or an invalid capture parameter.
                    // In all these cases we show an intent for the traditional file picker, filtered
                    // on accept type so launch an intent for both camcorder and video/* OPENABLE.
                    Intent chooser = createChooserIntent(createCamcorderIntent());
                    chooser.putExtra(Intent.EXTRA_INTENT, createOpenableIntent(videoMimeType));
                    startActivity(chooser);
                    return;
                }
            } else if (mimeType.equals(audioMimeType)) {
                if (mediaSource.equals(mediaSourceValueMicrophone)) {
                    // Specified 'audio/*' and requested microphone, so go ahead and launch the sound
                    // recorder.
                    startActivity(createSoundRecorderIntent());
                    return;
                } else {
                    // Specified just 'audio/*',  capture=filesystem of an invalid capture parameter.
                    // In all these cases so go ahead and launch an intent for both the sound
                    // recorder and audio/* OPENABLE.
                    Intent chooser = createChooserIntent(createSoundRecorderIntent());
                    chooser.putExtra(Intent.EXTRA_INTENT, createOpenableIntent(audioMimeType));
                    startActivity(chooser);
                    return;
                }
            }
            // No special handling based on the accept type was necessary, so trigger the default
            // file upload chooser.
            startActivity(createDefaultOpenableIntent());
        }
        private void startActivity(Intent intent) {
            try {
                mController.getActivity().startActivityForResult(intent, Controller.FILE_SELECTED);
            } catch (ActivityNotFoundException e) {
                // No installed app was able to handle the intent that
                // we sent, so fallback to the default file upload control.
                try {
                    mCaughtActivityNotFoundException = true;
                    mController.getActivity().startActivityForResult(createDefaultOpenableIntent(),
                            Controller.FILE_SELECTED);
                } catch (ActivityNotFoundException e2) {
                    // Nothing can return us a file, so file upload is effectively disabled.
                    Toast.makeText(mController.getActivity(), R.string.uploads_disabled,
                            Toast.LENGTH_LONG).show();
                }
            }
        }
        private Intent createDefaultOpenableIntent() {
            // Create and return a chooser with the default OPENABLE
            // actions including the camera, camcorder and sound
            // recorder where available.
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            Intent chooser = createChooserIntent(createCameraIntent(), createCamcorderIntent(),
                    createSoundRecorderIntent());
            chooser.putExtra(Intent.EXTRA_INTENT, i);
            return chooser;
        }
        private Intent createChooserIntent(Intent... intents) {
            Intent chooser = new Intent(Intent.ACTION_CHOOSER);
            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents);
            chooser.putExtra(Intent.EXTRA_TITLE,
                    mController.getActivity().getResources()
                            .getString(R.string.choose_upload));
            return chooser;
        }
        private Intent createOpenableIntent(String type) {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType(type);
            return i;
        }
        private Intent createCameraIntent() {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File externalDataDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM);
            File cameraDataDir = new File(externalDataDir.getAbsolutePath() +
                    File.separator + "browser-photos");
            cameraDataDir.mkdirs();
            mCameraFilePath = cameraDataDir.getAbsolutePath() + File.separator +
                    System.currentTimeMillis() + ".jpg";
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mCameraFilePath)));
            return cameraIntent;
        }
        private Intent createCamcorderIntent() {
            return new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        }
        private Intent createSoundRecorderIntent() {
            return new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        }
    }
	
}
