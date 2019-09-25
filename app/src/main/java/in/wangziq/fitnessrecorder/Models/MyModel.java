package in.wangziq.fitnessrecorder.Models;

public class MyModel {
        String time;
        String distanse;
        int id;

        public MyModel(){

        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public MyModel(int i, String string, String cursorString) {
            this.id = id;
            this.time = time;
            this.distanse = distanse;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getDistanse() {
            return distanse;
        }

        public void setDistanse(String distanse) {
            this.distanse = distanse;
        }


}
