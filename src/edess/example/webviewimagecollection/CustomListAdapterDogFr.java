package edess.example.webviewimagecollection;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapterDogFr extends ArrayAdapter<String> {
	private final Activity context;
	private final String[] friendDogsName;
	private final Integer[] img_id;
	private final String[] friend_age;
	private final String[] friendDogsBreed;
	private final String[] friendDogsPrice;
	private final String[] friendDogsSex;
	

	public CustomListAdapterDogFr(Activity context, String[] friendDogsName, Integer[] img_id, String[] friend_age,String[] friendDogsBreed,String[] friendDogsPrice,String[] friendDogsSex ) {
		super(context, R.layout.list_view_each_compan,friendDogsName);
		// TODO Auto-generated constructor stub
		
		this.context=context;
		this.friendDogsName=friendDogsName;
		this.img_id=img_id;
		this.friend_age=friend_age;
		this.friendDogsBreed=friendDogsBreed;
		this.friendDogsPrice=friendDogsPrice;
		this.friendDogsSex=friendDogsSex;
	}
	
	public View getView(int position,View view,ViewGroup parent) {
		LayoutInflater inflater=context.getLayoutInflater();
		View rowView=inflater.inflate(R.layout.list_view_each_compan, null,true);
		
		TextView dogName = (TextView) rowView.findViewById(R.id.textViewFriendName);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.ivPetFriend);
		TextView Friend_age = (TextView) rowView.findViewById(R.id.textViewFriendAge);
		TextView Friend_breed = (TextView) rowView.findViewById(R.id.tvFriendBreed);
		TextView Friend_price = (TextView) rowView.findViewById(R.id.tvFriendPrice);
		TextView Friend_sex = (TextView) rowView.findViewById(R.id.tvFriendSex);

		
		
		dogName.setText(friendDogsName[position]);
		imageView.setImageResource(img_id[position]);
		Friend_age.setText(friend_age[position]);
		Friend_breed.setText(friendDogsBreed[position]);
		Friend_price.setText(friendDogsPrice[position]);
		Friend_sex.setText(friendDogsSex[position]);

		
		
		return rowView;
		
	};
}
