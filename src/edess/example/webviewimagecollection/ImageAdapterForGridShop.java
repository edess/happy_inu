package edess.example.webviewimagecollection;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapterForGridShop extends BaseAdapter {
	// sourceVideo tutorial : https://www.youtube.com/watch?v=Zgx52Q9N3yQ
	
	private Context CTX; 
	private Integer image_id[]= {R.drawable.n_food_1, R.drawable.n_food_5, R.drawable.n_food_9,
								R.drawable.n_food_4, R.drawable.n_food_8, R.drawable.n_food_3,
								R.drawable.n_food_2, R.drawable.n_food_6, R.drawable.n_food_7};
	
	
	public ImageAdapterForGridShop(Context CTX){
		
		this.CTX=CTX; 
	
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return image_id.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ImageView img; 
		if(convertView==null){
			img= new ImageView(CTX);
			//img.setLayoutParams(new GridView.LayoutParams(300,300));
			img.setAdjustViewBounds(true);
			img.setScaleType(ImageView.ScaleType.CENTER_CROP);
			img.setPadding(8, 8, 8, 8);
		}
		else{
			img = (ImageView) convertView; 
		}
		img.setImageResource(image_id[position]);
		
		return img;
	}

}
