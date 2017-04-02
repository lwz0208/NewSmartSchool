package com.ding.chat.domain;

import java.util.List;

/**
 * Created by Erick on 2017/1/18.
 */
public class FlowWorkMenuList {


    /**
     * code : 200
     * ret : {"total_num":5,"workflow_def_list":[{"id":"587eccfc4a049527f4d23cfe","name":"院办议题","create_time":"2017-01-18T10:03:40.405000","status":1},{"id":"587ee56c4a049527f4d23cff","name":"申请人科室控件测试","create_time":"2017-01-18T11:47:56.558000","status":1},{"id":"587eee064a049527f4d23d00","name":"时间控件测试","create_time":"2017-01-18T12:24:38.762000","status":1},{"id":"587f15a24a049527f4d23d01","name":"时间范围测试","create_time":"2017-01-18T15:13:37.995000","status":1},{"id":"587f1b854a049527f4d23d04","name":"处方统计","create_time":"2017-01-18T15:38:45.166000","status":1}]}
     */

    private int code;
    /**
     * total_num : 5
     * workflow_def_list : [{"id":"587eccfc4a049527f4d23cfe","name":"院办议题","create_time":"2017-01-18T10:03:40.405000","status":1},{"id":"587ee56c4a049527f4d23cff","name":"申请人科室控件测试","create_time":"2017-01-18T11:47:56.558000","status":1},{"id":"587eee064a049527f4d23d00","name":"时间控件测试","create_time":"2017-01-18T12:24:38.762000","status":1},{"id":"587f15a24a049527f4d23d01","name":"时间范围测试","create_time":"2017-01-18T15:13:37.995000","status":1},{"id":"587f1b854a049527f4d23d04","name":"处方统计","create_time":"2017-01-18T15:38:45.166000","status":1}]
     */

    private RetBean ret;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public RetBean getRet() {
        return ret;
    }

    public void setRet(RetBean ret) {
        this.ret = ret;
    }

    public static class RetBean {
        private int total_num;
        /**
         * id : 587eccfc4a049527f4d23cfe
         * name : 院办议题
         * create_time : 2017-01-18T10:03:40.405000
         * status : 1
         */

        private List<WorkflowDefListBean> workflow_def_list;

        public int getTotal_num() {
            return total_num;
        }

        public void setTotal_num(int total_num) {
            this.total_num = total_num;
        }

        public List<WorkflowDefListBean> getWorkflow_def_list() {
            return workflow_def_list;
        }

        public void setWorkflow_def_list(List<WorkflowDefListBean> workflow_def_list) {
            this.workflow_def_list = workflow_def_list;
        }

        public static class WorkflowDefListBean {
            private String id;
            private String name;
            private String create_time;
            private int status;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }
    }
}
