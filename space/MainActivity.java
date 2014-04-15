package com.example.space;

import java.io.File;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private static int TAKE_PICTURE = 1;
	private Uri outputFileUri;
	Bitmap bm; 
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        takePicture();
        
        Button boton = (Button)findViewById(R.id.button1);
        boton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
               Intent intent = new Intent(MainActivity.this, MainActivity.class);
               startActivity(intent);
            }
         });
    }
    
    protected void takePicture(){
    	saveFullImage();
        bm = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/"+"muestra.jpg");
        procesarImagen(bm);
    }
    
    protected void procesarImagen(Bitmap bm){
    	if(bm == null){
    		TextView mensaje = (TextView)findViewById(R.id.mensaje);
        	mensaje.setText("NO SE DETECTO IMAGEN");
    		return;
    	}
    	
    	int width = bm.getWidth();
    	int height = bm.getHeight();
    	
    	int cont = 0;
    	int contW = 0;
    	int contN = 0;
    	int contH = 0;
    	
    	for(int i=0; i<width; i++)
    		for(int j=0; j<height; j++){
    			int pixelio = bm.getPixel(i, j);
    			
    	    	int r = (pixelio >> 16) & 0xff;
    		    int g = (pixelio >> 8) & 0xff;
    		    int b = (pixelio) & 0xff;
    		    
    		    
    			if(Math.abs(r-g)<=40 && Math.abs(b-g)<=40 && Math.abs(r-b)<= 40 && g>110) {///si es blanco, elimina pixeles blancos				    
   			    	contW++;			    		
   	
   			    }else if( (r<=55 && b<=55 && g<= 55) ){//si es negro
   			    	contN++;
   			    	contH++;
   	
   			    }else if(r>=150 && r<=200 && g>=150 && g<=200 && b <= 50 && r>g){
   			    	contN++;
   			    	contH++;			    	
   			    }  else{ 
   			    	contH++;
   			    }
    		}
    	//float val = (float) contW/(cont+1);
		float val2 = (float)contN/(contH+1);
    	
		String casos ="";
		
    	if(val2 == 0){
    		casos ="No ozone-induced injury";
    	}else if(val2 >=1 && val2 <=6){
    		casos ="Light ozone-induced foliar injury and beginning symptoms of injury";
    	}else if(val2>=7 && val2<=25){
    		casos ="Moderate ozone-induced injury";
    	}else if(val2>=26 && val2 <=50){
    		casos ="Moderately severe ozone-induced injured";
    	}else if(val2>=51 && val2<=75){
    		casos ="Severe ozone-induced injury";
    	}else{
    		casos = "Extremely severe ozone-induced foliar injury usually results in the death of the leaf";
    	}
    	
    	TextView mensaje = (TextView)findViewById(R.id.mensaje);
    	TextView danio = (TextView)findViewById(R.id.danio);
    	TextView caso = (TextView)findViewById(R.id.caso);
    	
    	
		mensaje.setText("Results");
    	danio.setText("Porcent: "+String.valueOf(val2) +" %");
    	caso.setText("Case: "+casos);
    	
    }
        
    private void saveFullImage() {
    	Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    	File file = new File(Environment.getExternalStorageDirectory(),	"muestra.jpg");
    	outputFileUri = Uri.fromFile(file);
    	intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
    	startActivityForResult(intent, TAKE_PICTURE);
	}
}
