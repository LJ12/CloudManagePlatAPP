package com.example.lijing.cloudmanageplatform.TeacherActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lijing.cloudmanageplatform.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import static android.graphics.Color.BLACK;

/**
 * Created by lijing on 2019/4/22.
 */

public class History_QRcode_Adapter extends BaseAdapter {
    Context context;
    List<String> list=new ArrayList<>();

    public  History_QRcode_Adapter(Context context,List list){
        this.context=context;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (!list.get(position).toString().equals("")) {
            if(convertView==null){
                viewHolder=new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView=inflater.inflate(R.layout.history_item,null,true);
                viewHolder.name=(TextView) convertView.findViewById(R.id.TV_HI_1);
                viewHolder.jieshu=(TextView) convertView.findViewById(R.id.TV_HI_2);
                viewHolder.start=(TextView) convertView.findViewById(R.id.TV_HI_3);
                viewHolder.end=(TextView) convertView.findViewById(R.id.TV_HI_4);
                viewHolder.qrcode=(ImageView)convertView.findViewById(R.id.IV_HI_1);
                convertView.setTag(viewHolder);
            }else{
                viewHolder=(ViewHolder) convertView.getTag();
            }
            viewHolder.name.setText(list.get(position).toString().split(" ")[0]);
            viewHolder.jieshu.setText(list.get(position).toString().split(" ")[1]);
            viewHolder.start.setText(list.get(position).toString().split(" ")[2]+" "+list.get(position).toString().split(" ")[3]);
            viewHolder.end.setText(list.get(position).toString().split(" ")[4]+" "+list.get(position).toString().split(" ")[5]);
            Create2QR2(list.get(position).toString().split(" ")[6],viewHolder.qrcode);
        }
        return convertView;
    }
    private Bitmap createQRCode(String url, int widthAndHeight)
            throws WriterException {
        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix matrix = new MultiFormatWriter().encode(url,
                BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight);

        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];
        //画黑点
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = BLACK;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
    //生成二维码的方法
    private void Create2QR2(String urls,ImageView imageView) {
        String uri = urls;
        int mScreenWidth = 0;
        Bitmap bitmap;
        try {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            mScreenWidth = dm.widthPixels;

            bitmap = createQRCode(uri, mScreenWidth);

            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    class ViewHolder{
        TextView name,jieshu,start,end;
        ImageView qrcode;
    }
}
