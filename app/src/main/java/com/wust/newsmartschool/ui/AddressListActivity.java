package com.wust.newsmartschool.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.AddressListAdapter;
import com.wust.newsmartschool.domain.AddressItem;

import java.util.ArrayList;


public class AddressListActivity extends AppCompatActivity {
    private ArrayList<AddressItem> addressList;
    private ListView listView;
    private AddressListAdapter addressListAdapter;
    private TextView department;
    private TextView phone;
    private Context context;
    private Toolbar toolbar;
    private String[] departmentList={"学校办公室（政策法规研究室）、校友会办公室","纪委办公室（监察处）","党委组织部（统战部）",
            "党委宣传部（新闻中心、党校）","党委学生工作部（武装部、学生工作处）","工会、妇女委员会","团委、学生会",
            "科学技术发展院","产学研与科技合作处","基础研究与高新技术处","基地建设与成果管理处","人文社会科学处","研究生院（党委研究生工作部）",
            "学位与学科建设处","培养教育处","招生就业处"," 人事处","教务处"," 教学质量监控与评估处","财务处","审计处",
            "实验室与设备管理处","基建与后勤管理处"," 国际交流合作处","离退休工作处","保卫处（党委保卫部)","住宅建设与改革领导小组办公室",
            "采购与招标管理办公室","工程训练中心","图书馆","档案馆","现代教育信息中心","高等教育研究所","学报编辑部","后勤集团","校医院",
            "资产经营有限公司"," 国际钢铁研究院","武钢—武科大钢铁新技术研究院","绿色制造与节能减排中心","高性能钢铁材料及其应用湖北省协同创新中心"};
    private String[] phoneList={"027-68862478","027-68862473","027-68862793","027-68862542","027-68862673","027-68863508",
            "027-68862339","027-68862153","027-68862412","027-68862219","027-68862621","027-68862717","027-68862830",
            "027-68862026","027-68862116","027-68862830","027-68862406","027-68862468","027-68862055","027-68862458",
            "027-68862466","027-68862205","027-68862819","027-68862606","027-68864266","027-68862246","027-68862967",
            "027-68862385","027-68893669","027-68862220","027-68862017","027-68862211","027-68862410","027-68862317",
            "027-68862221","027-68893271","027-68863373","027-68862772","027-68862296","027-68862815","027-68862266"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        context=getApplicationContext();
        Typeface typeface = Typeface.create("宋体", Typeface.BOLD);//参数后面一个表示设置粗体
        department=(TextView)findViewById(R.id.department);
        phone=(TextView)findViewById(R.id.phone);
        department.setTextSize(20);
        phone.setTextSize(20);
        department.setTypeface(typeface);
        phone.setTypeface(typeface);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView=(ListView)findViewById(R.id.addressList);
        getAddressListData();
        addressListAdapter=new AddressListAdapter(context, addressList);
        listView.setAdapter(addressListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                String strNumber = phoneList[position];
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + strNumber));
                AddressListActivity.this.startActivity(intent);
            }
        });
    }

    public void getAddressListData(){
        addressList=new ArrayList<AddressItem>();
        for(int i=0;i<departmentList.length;i++){

            AddressItem addressItem=new AddressItem();
            addressItem.setDepartment(departmentList[i]);
            addressItem.setPhone(phoneList[i]);
            addressList.add(addressItem);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

