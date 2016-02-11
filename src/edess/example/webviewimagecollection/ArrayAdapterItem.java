package edess.example.webviewimagecollection;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
  
// here's our beautiful adapter 
public class ArrayAdapterItem extends ArrayAdapter<ObjectItem> {
  
    Context mContext;
    int layoutResourceId;
    ObjectItem data[] = null;
    ImageView whichLevel; 
  
    public ArrayAdapterItem(Context mContext, int layoutResourceId, ObjectItem[] data) {
  
        super(mContext, layoutResourceId, data);
  
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    } 
  
    @Override 
    public View getView(int position, View convertView, ViewGroup parent) {
  
        /* 
         * The convertView argument is essentially a "ScrapView" as described is Lucas post  
         * http://lucasr.org/2012/04/05/performance-tips-for-androids-listview/ 
         * It will have a non-null value when ListView is asking you recycle the row layout.  
         * So, when convertView is not null, you should simply update its contents instead of inflating a new row layout. 
         */ 
        if(convertView==null){
            // inflate the layout 
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        } 
  
        // object item based on the position 
        ObjectItem objectItem = data[position];
  
        // get the TextView and then set the text (item name) and tag (item ID) values 
        TextView textViewItem = (TextView) convertView.findViewById(R.id.textViewItem);
        textViewItem.setText(objectItem.itemName);
        textViewItem.setTag(objectItem.itemId);
        whichLevel =(ImageView)convertView.findViewById(R.id.ivUpgradeLevelIcon); 
        
        String menuItemName=(String) textViewItem.getText();
        //Log.e("subMenu", menuItemName);
        
        ImageView menuIcon = (ImageView)convertView.findViewById(R.id.ivMenuIcons); 
        
        if(menuItemName=="Shopping" || menuItemName=="買い物"){
        	menuIcon.setImageResource(R.drawable.shop_icon);
        	whichLevel.setVisibility(View.INVISIBLE);
        }
        else if(menuItemName=="Companions" || menuItemName=="ペットの友達を獲得する"){
        	menuIcon.setImageResource(R.drawable.companion_icon);
        }
        else if(menuItemName=="Walking" || menuItemName=="ペットと散歩する"){
        	menuIcon.setImageResource(R.drawable.walking_icon);
        	whichLevel.setImageResource(R.drawable.icon_level3);
        }
        else if(menuItemName=="Health" ||menuItemName=="ペットの健康"){
        	menuIcon.setImageResource(R.drawable.health_icon);
        	whichLevel.setImageResource(R.drawable.icon_level4);
        }
        else if(menuItemName=="Places" || menuItemName=="ペットと共に有名な場所に訪れる"){
        	menuIcon.setImageResource(R.drawable.place_icon);
        	whichLevel.setImageResource(R.drawable.icon_level5);
        }
        else if(menuItemName=="Feelings" || menuItemName=="ペットの調子"){
        	whichLevel.setImageResource(R.drawable.icon_level7);
        }
        else if(menuItemName=="Accessories" || menuItemName=="ペットのアクセサリー"){
        	menuIcon.setImageResource(R.drawable.accessories_icon);
        	whichLevel.setImageResource(R.drawable.icon_level9);
        }
        else if(menuItemName=="Dog" || menuItemName=="私の犬"){
        	menuIcon.setImageResource(R.drawable.dog_icon);
        	whichLevel.setVisibility(View.INVISIBLE);
        }
        
       
  
        return convertView;
  
    } 
  
} 
