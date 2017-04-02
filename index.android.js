import { NativeModules } from 'react-native';
import React, { Component } from 'react';
import { Modal,AppRegistry,View,AsyncStorage, Text,TextInput,StyleSheet,ScrollView,ToastAndroid,BackAndroid,TouchableHighlight } from 'react-native';
import Header from './Header';
import Flowtree from './app/src/main/assets/flowtree.js';

var userId = "";
var sid = "";
var fid = "";
var workID = "";
var fk_flow = "";
var shenheren = "";
var Suffix = "";
var starterName="";//add starterName

//测试IP
//var rootUrl="http://120.132.85.24:8080/";
//正式IP
var rootUrl="http://219.140.188.58:8080/";



var ispass = 0;

function getNowFormatDate() {
    var date = new Date();
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
        + " " + date.getHours() + seperator2 + date.getMinutes()
        + seperator2 + date.getSeconds();
    return currentdate;
}

class AwesomeProject extends Component {

    //进行从Activity中获取数据传输到JS
    componentDidMount(){
        NativeModules.IntentModule.dataToJS(
            (msid,muserId,mfk_flow,mshenheren,mworkID,mispass,mstarterName) => {
                sid=msid;
                userId=muserId;
                fk_flow=mfk_flow;
                workID=mworkID;
                shenheren=mshenheren;
                ispass=mispass;
                starterName=mstarterName;
                this.setState({starterName:mstarterName});

                 fid = '';
                 Suffix = '';

                //获取fk_node、fid
                let url = 'userId='+userId+'&sid='+sid+'&workID='+workID;
                fetch(rootUrl+'jflow-web/OA/Jflow/2056?'+url, {
                    method: 'GET',
                }).then((response) => {
                    if (response.ok) {
                        return response.json()
                    } else {
                        console.error('服务器繁忙，请稍后再试；\r\nCode:' + response.status)
                    }
                }).then((res) => {
                    console.log(res);
                    let data = res.data[0];
                    var fk_node = data.fk_node;
                    if(fk_node == 602){
                        Suffix = "jw";
                    }else if(fk_node == 604){
                        Suffix = "ld";
                    }else{Suffix = "xxk";}
                    fid = data.fid;

                    //获取表单详情
                    let url = 'userId='+userId+'&sid='+sid+'&fid='+fid;
                    AsyncStorage.setItem('url',url);
                    fetch(rootUrl+'jflow-web/OA/Jflow/2055?'+url, {
                        method: 'GET',
                        headers: {
                           "Content-type": "application/x-www-form-urlencoded;charset=utf-8"
                        },
                    }).then((response) => {
                        if (response.ok) {
                            return response.json()
                        } else {
                            console.error('服务器繁忙，请稍后再试；\r\nCode:' + response.status)
                        }
                    }).then((res) => {
                        let data = res.data[0];
                        this.setState({starttime: data.starttime});
                        this.setState({endtime: data.endtime});
                        this.setState({liushuihao: data.liushuihao});
                        this.setState({content: data.content});

                        //处理flowtree
                        this.state.flow.push({
                            info:"流程开始",
                            time: data.starttime,
                            ISPASS :1
                        })
                        let info = [];
                        if (data.ispass_ld === undefined) {
                            return;
                        }
                        if (data.ispass_ld != 0) {
                            var ispass = "已通过";
                            var time = data.passtime_ld;
                            if (data.ispass_ld == -1) {
                                ispass = "已驳回";
                            }
                            info.push(time + ": 领导" + data.shenheren_ld + ispass);
                            info.push("\t意见：" + data.yijian_ld);
                        }
                        if (data.ispass_xxk != 0) {
                            var ispass = "已通过";
                            var time = data.passtime_xxk;
                            if (data.ispass_xxk == -1) {
                                ispass = "已驳回";
                            }
                            info.push(time + ": 信息科" + data.shenheren_xxk + ispass);
                            info.push("\t意见：" + data.yijian_xxk);
                        }
                        if (data.ispass_jw != 0) {
                            var ispass = "已通过";
                            var time = data.passtime_jw;
                            if (data.ispass_jw == -1) {
                                ispass = "已驳回";
                            }
                            info.push(time + ": 纪委" + data.shenheren_jw + ispass);
                            info.push("\t意见：" + data.yijian_jw);
                        }

                        let ISPASS = 0; //统计是否通过
                        if (info !== "") {

                            if (data.ispass_ld === 1 && data.ispass_xxk === 1 && data.ispass_jw === 1) {
                                ISPASS = 1;
                            }
                            else if (data.ispass_ld === 0 || data.ispass_xxk === 0 || data.ispass_jw === 0) {
                                ISPASS = 0;

                            }
                            else {
                                ISPASS = -1;
                            }
                            this.state.flow.push({info: info.join("\n"), ISPASS});
                        }
                        //}
                        if (data.workstarttime)
                            this.state.flow.push({info: "流程结束", ISPASS: 1, time: data.workstarttime});

                        this.setState({flowtree:  (
                            <Flowtree flow={this.state.flow}/>
                        )});
                    })
                })
              },
               (result) => {
                ToastAndroid.show('JS错误信息为:'+result,ToastAndroid.SHORT);
              });
    }

    constructor(props){
        super(props);
        this.state = {
            //TODO: 数据格式帮我修改一下
            test:'432432',
            starttime:"",
            endtime:"",
            liushuihao:"",
            content:"",
            suggestion:"",
            flow: [],
            animationType: false,
            modalVisible: false,
            transparent: true,
            flowtree : (<View></View>)
        };




        this._onPressOkButton = this._onPressOkButton.bind(this);
        this._onPressNoButton = this._onPressNoButton.bind(this);
    }

    _setModalVisible(visible) {
        this.setState({modalVisible: visible});
    }

    _onPressOkButton (){
        this._setModalVisible(true);
        this.setState({op: 0});

    }

    _onPressNoButton(){
        this._setModalVisible(true);
        this.setState({op: 1});
    }
    _onBack(){
        BackAndroid.exitApp();
    }
    _submit(){
        //TODO submit data to server &&& navigator.pop
        //alert(this.state.op);
        var time = getNowFormatDate();
        var isPass = 0;
        if(this.state.op == 0){
            isPass = '1';
        }else if(this.state.op == 1){
            isPass = '-1';
        }

        //提交
        let jsonstr='{"shenheren_'+Suffix+'":"'+shenheren+'","ispass_'+Suffix+'":"'+isPass+'","yijian_'+Suffix+'":"'+this.state.suggestion+'","passtime_'+Suffix+'":"'+time+'"}';
        let data2 = 'userId='+userId+'&sid='+sid+'&workID='+workID+'&fk_flow='+fk_flow+'&jsonstr='+jsonstr;
        fetch(rootUrl+'jflow-web/OA/Jflow/2054', {
            method: 'POST',
            headers: {
                "Content-type": "application/x-www-form-urlencoded;charset=utf-8"
            },
            body: data2
        }).then((response) => {
            if (response.ok) {
                return response.json()
            } else {
                console.error('服务器繁忙，请稍后再试；\r\nCode:' + response.status)
            }
        }).then((res) => {
            console.log(res);
            isPass = 1;
            if(res.code == 1){
                alert("审批成功！");
            }else{
                alert("已审批过，请不要重复审批！");
            }
            ispass = 2;
            this._setModalVisible(false);
            this.setState({op: undefined});
        })
    }
    _canceal(){
        this._setModalVisible(false);
        this.setState({op: undefined});
    }
    render() {
        let tf_flow_op
        if(ispass == 1) {
            tf_flow_op = (
                <View>
                    <View style={styles.tf_flow_op}>
                        <TouchableHighlight style={styles.tf_flow_ok} activeOpacity={0.3} underlayColor="green"
                                            onPress={this._onPressOkButton}>
                            <Text style={styles.tf_flow_btn_tex}>同意</Text>
                        </TouchableHighlight>
                        <TouchableHighlight style={styles.tf_flow_no} activeOpacity={0.3} underlayColor="orange"
                                            onPress={this._onPressNoButton}>
                            <Text style={styles.tf_flow_btn_tex}>驳回</Text>
                        </TouchableHighlight>
                    </View>
                    < Modal visible={this.state.modalVisible}
                            animationType='fade'
                            transparent={true}
                            onRequestClose={() =>console.log('onRequestClose...')}
                        >
                        <View style={[styles.tf_flow_confirm, styles.modalBackgroundStyle]}>
                            <View style={styles.tf_flow_confirm_inner}>
                                <Text style={styles.tf_flow_title}>{this.state.op === 0 ? "同意" : "驳回"}</Text>
                                <View style={{paddingHorizontal: 13}}>
                                    <TextInput
                                        style={styles.tf_flow_confirm_txtinput}
                                        placeholder = "请填写意见"
                                        //multiline = {true}
                                        clearButtonMode = "while-editing"
                                        onEndEditing={(event) => this.setState({suggestion: event.nativeEvent.text})}
                                        />
                                </View>
                                <View style={styles.tf_flow_confirm_op}>
                                    <TouchableHighlight style={[styles.tf_flow_submitBtn, {backgroundColor: this.state.op === 0 ? "#339900" : "#ff9900"}]} activeOpacity={0.3} underlayColor="green" onPress={this._submit.bind(this)}>
                                        <Text style={styles.tf_flow_btn_tex}>{this.state.op === 0 ? "同意" : "驳回"}</Text>
                                    </TouchableHighlight>
                                    <TouchableHighlight style={styles.tf_flow_cancealBtn} activeOpacity={0.3} underlayColor="gray"  onPress={this._canceal.bind(this)}>
                                        <Text style={styles.tf_flow_btn_tex}>取消</Text>
                                    </TouchableHighlight>
                                </View>
                            </View>
                        </View>
                    </Modal>
                </View>
            )
        }
        else{
            tf_flow_op= (
                <View>
                    {this.state.flowtree}
                </View>
            )
        }
        return (

            <View style={{flex: 1, flexDirection: 'column'}}>
                <Header onBack={this._onBack.bind(this)} name="返回"></Header>
                <ScrollView style={styles.tf_flow_page}>
                    <View  style={styles.tf_flow_info}>
                        <Text style={styles.tf_flow_label}>流水号：</Text>
                        <Text>{this.state.liushuihao}</Text>
                        <Text style={styles.tf_flow_label}>发起时间：</Text>
                        <Text>{this.state.starttime}</Text>
                        <Text style={styles.tf_flow_label}>发起人：</Text>
                        <Text>{this.state.starterName}</Text>
                        <Text style={styles.tf_flow_label}>流程详情：</Text>
                        <Text>{this.state.content}</Text>
                        <Text style={styles.tf_flow_label}>统方开始时间：</Text>
                        <Text>{this.state.workstarttime}</Text>
                        <Text style={styles.tf_flow_label}>统方截止时间：</Text>
                        <Text>{this.state.workendtime}</Text>

                    </View>
                    {tf_flow_op}

                </ScrollView>
            </View>
        );
    }
}

const styles = StyleSheet.create({
    tf_flow_page: {
        flex: 9,
        backgroundColor: "#DDD",
        overflow:'visible'
    },
    tf_flow_info: {
        backgroundColor: "#FFF",
        marginVertical: 20,
        padding: 20,
        paddingTop: 0
    },
    tf_flow_label: {
        fontSize: 15,
        fontWeight:"bold",
        marginTop: 15,
        marginBottom: 15,
    },
    tf_flow_op: {
        marginVertical:20,
        flexDirection: "row",
        justifyContent: "space-around"
    },
    tf_flow_ok: {
        backgroundColor:"#339900",
        borderRadius: 4,
        width:160,
        height:38,
        padding:6
    },
    tf_flow_no: {
        backgroundColor:"#ff9900",
        borderRadius: 4,
        width:160,
        height:38,
        padding:6
    },
    tf_flow_btn_tex: {
        color: "#FFF",
        fontSize: 18,
        textAlign: "center",
    },
    tf_flow_confirm: {
        flex: 1,
        justifyContent: 'center',
        padding: 20
    },
    tf_flow_confirm_inner: {
        backgroundColor: '#FFF',
        paddingVertical:20,
        paddingHorizontal:10,
        borderRadius:6,
    },
    tf_flow_title: {
        textAlign: "center",
        fontSize: 18,
        fontWeight:"bold",
    },
    tf_flow_confirm_txtinput: {
        height: 80,
        borderWidth: 0.5,
        borderColor: '#0f0f0f',
        flex: 1,
        fontSize: 13,
    },
    tf_flow_confirm_op: {
        marginVertical:5,
        flexDirection: "row",
        justifyContent: "space-around"
    },
    tf_flow_submitBtn: {
        backgroundColor:"#339900",
        borderRadius: 4,
        width:120,
        height:38,
        padding:6
    },
    tf_flow_cancealBtn: {
        backgroundColor:"gray",
        borderRadius: 4,
        width:120,
        height:38,
        padding:6
    },
    modalBackgroundStyle: {
        backgroundColor:'rgba(0, 0, 0, 0.5)'
    }
});

// 注册应用(registerComponent)后才能正确渲染
// 注意：只把应用作为一个整体注册一次，而不是每个组件/模块都注册
AppRegistry.registerComponent('AwesomeProject', () => AwesomeProject);




