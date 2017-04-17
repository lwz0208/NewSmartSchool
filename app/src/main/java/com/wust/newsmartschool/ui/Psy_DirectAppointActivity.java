package com.wust.newsmartschool.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.domain.PsychologistInfoItem;
import com.wust.newsmartschool.utils.GlobalVar;
import com.wust.newsmartschool.utils.HttpServer;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;


/*涓�閿绾�
 * */
public class Psy_DirectAppointActivity extends Activity {
    // 鏍囬鎺т欢
    private TextView head;
    private ImageView back;
    // 琛ㄥ崟鎺т欢
    private EditText nameEditText;
    private RadioGroup sexGroup;
    private Spinner expSpinner;
    private EditText dateEditText;
    private Spinner timeSpinner;
    private EditText telEditText;
    private EditText expmsgEditText;
    private Button sendButton;
    // 甯搁噺
    private static final int SEND_OK = 1;// 鎻愪氦鎴愬姛
    private static final int SEDN_FAIL = 2;// 鎻愪氦澶辫触
    private static final int MSG_ERROR = 3;// 琛ㄥ崟濉啓閿欒
    private static final int GET_LIST_OK = 4;// 鑾峰緱缃戠粶鏁版嵁鈥滀笓瀹朵俊鎭�濇垚鍔�
    private static final int GET_PSYLIST_FAIL = 5;// 鑾峰緱涓撳鍒楄〃澶辫触
    private static final int HAVE_SENT = 6;// 宸茬粡鎻愪氦杩囬绾︼紝璇峰嬁閲嶅鎻愪氦
    private static final int UID_LOST=7;//瀛﹀彿寮傚父锛屽彲鑳戒涪澶�
    // 鍏朵粬
    private Calendar myCalendar;
    // 琛ㄥ崟鏁版嵁
    private String nameString = null;
    private String sexString = "鐢�";
    private int expid = 0;
    private String wid = null;
    private String dateString = null;
    private String time = null;
    private String telString = null;
    private String expString = "鏃犲娉ㄤ俊鎭�";
    // 缃戠粶鏁版嵁
    private ArrayList<PsychologistInfoItem> dataArrayList = null;
    // 涓嬫媺鍒楄〃鈥滀笓瀹堕�夋嫨鈥濈殑閫傞厤鍣�
    private ArrayAdapter<String> myaAdapter;
    ArrayList<String> expertnameList = new ArrayList<String>();

    private Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case SEND_OK:
                    Toast.makeText(Psy_DirectAppointActivity.this,
                            "棰勭害鍗曟彁浜ゆ垚鍔燂紝璇峰湪\"鎴戠殑棰勭害\"涓煡鐪嬮绾︾姸鎬侊紒", Toast.LENGTH_SHORT).show();
                    // Toast.makeText(
                    // Psy_DirectAppointActivity.this,
                    // nameString + "\n" + sexString + "\n" + wid + "\n"
                    // + dateString + "\n" + time + "\n" + telString,
                    // Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case SEDN_FAIL:
                    Toast.makeText(Psy_DirectAppointActivity.this, "鎻愪氦澶辫触锛岃妫�鏌ョ綉缁滐紒",
                            Toast.LENGTH_SHORT).show();
                    break;
                case GET_PSYLIST_FAIL:
                    Toast.makeText(Psy_DirectAppointActivity.this,
                            "鑾峰緱涓撳鍒楄〃澶辫触锛岃妫�鏌ョ綉缁滐紒", Toast.LENGTH_SHORT).show();
                    break;
                case MSG_ERROR:
                    Toast.makeText(Psy_DirectAppointActivity.this,
                            "鎮ㄦ彁浜ょ殑鏁版嵁涓嶅畬鏁达紝璇锋鏌ヨ〃鍗曪紒", Toast.LENGTH_SHORT).show();
                    break;
                case GET_LIST_OK:
                    for (int i = 0; i < dataArrayList.size(); i++) {
                        expertnameList.add(dataArrayList.get(i).getName());
                    }
                    myaAdapter = new ArrayAdapter<String>(
                            Psy_DirectAppointActivity.this,
                            R.layout.psy_spinner_board, expertnameList);
                    myaAdapter.setDropDownViewResource(R.layout.spinner_item);
                    expSpinner.setAdapter(myaAdapter);
                    break;
                case HAVE_SENT:
                    Toast.makeText(Psy_DirectAppointActivity.this,
                            "鎮ㄦ湁姝ｅ湪澶勭悊鐨勯绾︼紝璇峰嬁閲嶅鎻愪氦锛�", Toast.LENGTH_LONG).show();
                    break;
                case UID_LOST:
                    Toast.makeText(Psy_DirectAppointActivity.this,
                            "鎮ㄧ殑瀛﹀彿淇℃伅涓㈠け锛岃閲嶆柊鐧诲叆锛�", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_psy__direct_appoint);
        initView();
        getExpertinfoList();// 璇锋眰鑾峰緱涓撳淇℃伅鐢ㄦ潵濉厖鈥滀笓瀹堕�夋嫨鈥�
        // 鎬у埆閫夋嫨
        sexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                int radiobuttonid = group.getCheckedRadioButtonId();
                RadioButton rbButton = (RadioButton) Psy_DirectAppointActivity.this
                        .findViewById(radiobuttonid);
                sexString = rbButton.getText().toString();
            }
        });
        // 棰勭害鏃堕棿
        dateEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            Psy_DirectAppointActivity.this, DateSet, myCalendar
                            .get(Calendar.YEAR), myCalendar
                            .get(Calendar.MONTH), myCalendar
                            .get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();
                }
            }
        });
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Psy_DirectAppointActivity.this, DateSet, myCalendar
                        .get(Calendar.YEAR), myCalendar
                        .get(Calendar.MONTH), myCalendar
                        .get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        expSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                expid = arg2;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        // 鍙戦�佹寜閽�
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (dataArrayList != null) {
                    //棣栧厛鍒ゆ柇GlobalVar涓殑瀛﹀彿鏄惁鍋ュ湪
                    if(GlobalVar.userid!=null||GlobalVar.userid.equals("")){
                        //nameEditText.setTypeface(Typeface.);
                        nameString = nameEditText.getText().toString();

//						try {
//							URLDecoder.decode(nameString, "utf-8");
//						} catch (UnsupportedEncodingException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
//						try {
//							nameString=new String(nameString.getBytes("iso-8859-1"), "utf-8");
//						} catch (UnsupportedEncodingException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
                        wid = dataArrayList.get(expid).getId();
                        dateString = dateEditText.getText().toString();

                        String temp = timeSpinner.getSelectedItem().toString();
                        expString = expmsgEditText.getText().toString();
                        if (temp.equals("涓婂崍")) {
                            time = "1";
                        } else {
                            time = "2";
                        }
                        telString = telEditText.getText().toString();
                        if (nameString == null || sexString == null || wid == null
                                || dateString == null || time == null
                                || telString == null || nameString.equals("")
                                || sexString.equals("") || wid.equals("")
                                || dateString.equals("") || time.equals("")
                                || telString.equals("")) {
                            // 涓嶅彲鎻愪氦
                            myHandler.sendEmptyMessage(MSG_ERROR);
                        } else {
                            try{
                                // 鍙互鎻愪氦
                                sendMsg(GlobalVar.userid, URLEncoder.encode(nameString,"gbk"), sexString,
                                        telString, wid, dateString, time, URLEncoder.encode(expString,"gbk"));
                            }catch(UnsupportedEncodingException e1){
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }

                        }
                    }else{
                        //瀛﹀彿鍥犱负鏌愮鎯呭喌琚竻绌轰簡
                        myHandler.sendEmptyMessage(UID_LOST);
                    }

                } else {
                    myHandler.sendEmptyMessage(GET_PSYLIST_FAIL);
                }
            }
        });

    }

    private void initView() {
        // TODO Auto-generated method stub
        head = (TextView) findViewById(R.id.head_title);
        back = (ImageView) findViewById(R.id.goback);
        head.setText("棰勭害鍗�");
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Psy_DirectAppointActivity.this.finish();
            }
        });
        nameEditText = (EditText) findViewById(R.id.reservation_input_name);
        nameEditText.setText(GlobalVar.username);
        sexGroup = (RadioGroup) findViewById(R.id.reservation_sex_radiogroup);
        expSpinner = (Spinner) findViewById(R.id.appointment_doctor_select);
        dateEditText = (EditText) findViewById(R.id.appointment_date);
        timeSpinner = (Spinner) findViewById(R.id.appointment_time_select);
        telEditText = (EditText) findViewById(R.id.reservation_input_phone);
        expmsgEditText = (EditText) findViewById(R.id.reservation_exp_msg);
        sendButton = (Button) findViewById(R.id.reservation_appointment_send_appointment);
        // 璁剧疆
        dateEditText.setInputType(InputType.TYPE_NULL);
        myCalendar = Calendar.getInstance();
    }

    // 閲嶈鏂规硶锛氬彂閫佽〃鍗�
    private void sendMsg(final String uid, final String name, final String sex,
                         final String telephone, final String wid, final String date,
                         final String time, final String exp) {
        Runnable runnable = new Runnable() {
            public void run() {

                HttpServer httpServer = new HttpServer();

                String urlString = "http://202.114.242.198:8090/makeappointment.php?uid="
                        + uid
                        + "&name="
                        + name
                        + "&sex="
                        + sex
                        + "&telephone="
                        + telephone
                        + "&wid="
                        + wid
                        + "&date="
                        + date
                        + "&time=" + time + "&exp=" +exp;

                String backmsg = httpServer.getData(urlString);
                if(backmsg!=null){
                    if (backmsg.equals("1")) {// 鎻愪氦鎴愬姛
                        myHandler.sendEmptyMessage(SEND_OK);
                    } else if (backmsg.equals("0")) {
                        myHandler.sendEmptyMessage(HAVE_SENT);
                    }
                }else {// 鎻愪氦澶辫触锛屽彲鑳界殑鍘熷洜锛�1銆佹病缃戠粶2銆佺綉閫熶笉濂�3銆侀噸澶嶆彁浜�
                    myHandler.sendEmptyMessage(SEDN_FAIL);
                }

            }
        };
        new Thread(runnable).start();
    }

    /**
     * @description 鏃ユ湡璁剧疆鍖垮悕绫�
     */
    DatePickerDialog.OnDateSetListener DateSet = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // 姣忔淇濆瓨璁剧疆鐨勬棩鏈�
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String str = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            if (dateEditText.isFocused()) {
                dateEditText.setText(str);
            }
        }
    };

    private void getExpertinfoList() {
        Runnable runnable = new Runnable() {
            public void run() {
                HttpServer httpServer = new HttpServer();
                String urlString = "http://202.114.242.198:8090/psyconcertinfo.php";
                String jsonString = httpServer.getData(urlString);
                if (jsonString != null) {
                    dataArrayList = httpServer.getExpertInfoList(jsonString);
                    myHandler.sendEmptyMessage(GET_LIST_OK);
                }
            }
        };
        new Thread(runnable).start();
    }
}

