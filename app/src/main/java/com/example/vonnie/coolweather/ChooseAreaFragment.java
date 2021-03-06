package com.example.vonnie.coolweather;

import android.app.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vonnie.coolweather.data.City;
import com.example.vonnie.coolweather.data.County;
import com.example.vonnie.coolweather.data.Province;
import com.example.vonnie.coolweather.util.HttpUtil;
import com.example.vonnie.coolweather.util.Utility;

import org.litepal.LitePal;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChooseAreaFragment extends Fragment{
    public static final int LEVEL_PROVINCE=0;
    public static final int LEVER_CITY=1;
    public static final int LEVER_COUNTY=2;

   // private ProgressBar progressBar;
    //ProgressDialog
    private TextView titleText;
    private Button backButton;
    private ListView  listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList=new ArrayList<>();
    //省列表
    private List<Province> provinceList;
    //市列表
    private List<City> cityList;
    //县列表
    private List<County> countyList;

    //选中的省份
    private Province selectedProvince;
    //选中的城市
    private City selectedCity;
    //当前选中的级别
    private int currentLevel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //将布局填充成View对象(View类的方法inflate和LayoutInflater类的inflate方法)
        View view = inflater.inflate(R.layout.choose_area, container, false);
        titleText = (TextView) view.findViewById(R.id.title_text);
        backButton = (Button) view.findViewById(R.id.back_button);
        listView = (ListView) view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentLevel==LEVEL_PROVINCE){
                    selectedProvince=provinceList.get(position);
                    queryCities();

                }
                else if(currentLevel==LEVER_CITY){
                    selectedCity=cityList.get(position);
                    queryCounties();
                }else if(currentLevel==LEVER_COUNTY){
                    String weatherId=countyList.get(position).getweatherId();
                    if(getActivity()instanceof MainActivity) {
                        Intent intent = new Intent(getActivity(), WeatherActivity.class);
                        intent.putExtra("weather_id", weatherId);
                        startActivity(intent);
                        getActivity().finish();
                    }else if(getActivity() instanceof WeatherActivity){
                        WeatherActivity activity=(WeatherActivity)getActivity();
                        activity.drawerLayout.closeDrawers();
                        activity.swipeRefreshLayout.setRefreshing(true);
                        activity.requestWeather(weatherId);
                    }
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentLevel==LEVER_COUNTY){
                     queryCities();
                }else if(currentLevel==LEVER_CITY){
                      queryProvinces();
                }
            }
        });
        queryProvinces();
    }

    //查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器上查询
    private void queryProvinces(){
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        provinceList= LitePal.findAll(Province.class);
        if(provinceList.size()>0){
            dataList.clear();
            for(Province province:provinceList){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            //通过一个外部的方法控制如果适配器的内容改变时需要强制调用getView来刷新每个Item的内容
            listView.setSelection(0);
            currentLevel=LEVEL_PROVINCE;
        }else{
            String address="http://guolin.tech/api/china";
            queryFromServer(address,"province");
        }
    }

    private void queryCities(){
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList=LitePal.where("provinceid=?",String.valueOf(selectedProvince.getId())).find(City.class);
        if(cityList.size()>0){
            dataList.clear();
            for(City city:cityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVER_CITY;
        }else{
            int provinceCode=selectedProvince.getProvinceCode();
            String address="http://guolin.tech/api/china/"+provinceCode;
            queryFromServer(address,"city");
        }

    }

    private void queryCounties(){
             titleText.setText(selectedCity.getCityName());
             backButton.setVisibility(View.VISIBLE  );
             countyList=LitePal.where("cityid=?",String.valueOf(selectedCity.getId())).find(County.class);
             if(countyList.size()>0){
                 dataList.clear();
                 for(County county:countyList){
                     dataList.add(county.getCountyName());
                 }
                 adapter.notifyDataSetChanged();
                 listView.setSelection(0);
                 currentLevel=LEVER_COUNTY;
             }else{
                 int provinceCode=selectedProvince.getProvinceCode();
                 int cityCode=selectedCity.getCityCode();
                 String address="http://guolin.tech/api/china/"+provinceCode+"/"+cityCode;;
                 queryFromServer(address,"county");
             }
    }


    //根据传入的地址和类型从服务器上查询省市县数据
    private void queryFromServer(String address, final String type) {

        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            //相应的数据会回调到onResponse()中
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                if ("province".equals(type)) {
                    result = Utility.handleProvinceResponse(responseText);   //解析和处理服务器返回的数据
                } else if ("city".equals(type)) {
                    result = Utility.handleCityResponse(responseText, selectedProvince.getId());
                } else if ("county".equals(type)) {
                    result = Utility.handleCountyResponse(responseText, selectedCity.getId());
                }
                if (result) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {        //从子线程切换到主线程

                            if ("province".equals(type)) {
                                queryProvinces();                   //重新加载省级数据
                            } else if ("city".equals(type)) {
                                queryCities();
                            } else if ("county".equals(type)) {
                                queryCounties();
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                // 閫氳繃runOnUiThread()鏂规硶鍥炲埌涓荤嚎绋嬪鐞嗛�昏緫
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


}
