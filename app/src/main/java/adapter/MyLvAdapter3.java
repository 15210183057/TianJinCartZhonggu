package adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a123456.tianjincartzhonggu.R;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import bean.BuCartListBean;
import bean.UserBean;
import utils.Mydialog;
import jiekou.getInterface;
import utils.newAlert;

public class MyLvAdapter3 extends BaseAdapter{
    private List<BuCartListBean> list;
    private Context ctx;
    int count;
    private MyBroad myBroad;
    Mydialog mydialog;
    public MyLvAdapter3 (List<BuCartListBean>list, Context ctx){
        this.ctx=ctx;
        this.list=list;
        myBroad=new MyBroad();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("deleteitem");
        ctx.registerReceiver(myBroad,intentFilter);
        mydialog=new Mydialog(ctx,"正在下架，请稍等...");

    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view==null){
            view= View.inflate(ctx, R.layout.item_mylv2,null);
            holder=new ViewHolder();
            holder.tv_name=view.findViewById(R.id.tv_name_mylvitem);
            holder.tv_num1=view.findViewById(R.id.tv_num_mylvitem);
            holder.tv_company=view.findViewById(R.id.tv_company_mylvitem);
            holder.tv_num2=view.findViewById(R.id.tv_num2_mylvitem);
            holder.img_item_mylv=view.findViewById(R.id.img_item_mylv);
            holder.btn_xiajia=view.findViewById(R.id.btn_xiajia);
            holder.tv_user=view.findViewById(R.id.tv_user_itemylv2);
            holder.tv_price=view.findViewById(R.id.tvprice_mylvitem);
            holder.tv_time=view.findViewById(R.id.tv_time_itemylv2);
            holder.tv_model_mylvitem=view.findViewById(R.id.tv_model_mylvitem);
            view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }
        if(TextUtils.isEmpty(list.get(i).brandName)||list.get(i).brandName.equals("null")){
            holder.tv_name.setText("--");
        }else {
            String str=list.get(i).brandName;
            holder.tv_name.setText(str);
        }
//        for(int h=0;h<str.length();h++){
//            char  item =  str.charAt(h);
//            if(item<='Z'&&item>='A'){
//                str=  str.replace(String.valueOf(item),"");
//                h--;
//            }else if(item>='0'&&item<='9'){
//                str=  str.replace(String.valueOf(item),"");
//                h--;
//            }
//        }
//        str=str.replace("（","");
        holder.tv_company.setText(list.get(i).name);
        holder.tv_num1.setText(list.get(i).vin);

        holder.tv_num2.setText(list.get(i).licensePlate);

        holder.tv_model_mylvitem.setText(list.get(i).carName);
        if(list.get(i).status.equals("0")){
            holder.btn_xiajia.setText("已下架");
//            holder.btn_xiajia.setClickable(false);
//            holder.btn_xiajia.setOnClickListener(null);
            holder.btn_xiajia.setBackgroundResource(R.color.bule);
            holder.btn_xiajia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(ctx,"点击下架",Toast.LENGTH_LONG);
//                    Log.e("TAG","点击下架=="+i);
//                    newAlert alert=new newAlert(ctx,i);
//                    alert.show();
                }
            });
        }else{
            holder.btn_xiajia.setText("下架");
            holder.btn_xiajia.setBackgroundResource(R.drawable.denglu_bg);
            holder.btn_xiajia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(ctx,"点击下架",Toast.LENGTH_LONG);
                    Log.e("TAG","点击下架=="+i);
                    h=0;
                    newAlert alert=new newAlert(ctx,i);
                    alert.show();
                }
            });

        }
//        holder.btn_xiajia.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(ctx,"点击下架",Toast.LENGTH_LONG);
//                Log.e("TAG","点击下架=="+i);
//                newAlert alert=new newAlert(ctx,i);
//                alert.show();
//            }
//        });
        holder.tv_time.setText("采集时间："+list.get(i).time);
        holder.tv_user.setText("采集员："+ UserBean.username);
        holder.tv_price.setText("价格："+list.get(i).price+"万");
        Glide.with(ctx).load(list.get(i).img1+"?x-oss-process=style/233_162").placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(holder.img_item_mylv);
        return view;
    }
    public class ViewHolder{
        TextView tv_name,tv_company,tv_num1,tv_num2;
        private ImageView img_item_mylv;
        TextView btn_xiajia,tv_user,tv_time,tv_price;
        TextView tv_model_mylvitem;
    }
    private void Delete(final int i){
        mydialog.show();
        Log.e("TAG","走几遍");
        RequestParams params=new RequestParams(getInterface.UpCartData);
        params.addBodyParameter("id",list.get(i).ListID);
        params.addBodyParameter("status","0");
        params.addBodyParameter("revise_admin",UserBean.id);
        Log.e("TAG","下架url=="+params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG","下架成功=i"+i+"=="+result);
                //刷新列表也
                Intent intent=new Intent();
                intent.setAction("delete");
                ctx.sendBroadcast(intent);
                mydialog.dismiss();
//                notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if(!TextUtils.isEmpty(ex.getMessage().toString())){
                    mydialog.dismiss();
                    Log.e("TAG","error=="+ex.getMessage().toString());
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
    int h=0;
    private class MyBroad extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
           String i = intent.getStringExtra("i");
           Log.e("TAG","下架接收广播=h="+h+"======"+(intent.getAction().equals("deleteitem")&&!TextUtils.isEmpty(i)));
            if(h==0){
                if(intent.getAction().equals("deleteitem")&&!TextUtils.isEmpty(i)){
                    Delete(Integer.parseInt(i));
                }
                h++;
            }

            Log.e("TAG","接受广播==="+i+"==h="+h);

        }
    }

}
