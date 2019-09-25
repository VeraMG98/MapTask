package in.wangziq.fitnessrecorder.DB;

import java.util.List;

import in.wangziq.fitnessrecorder.Models.MyModel;

public interface DBInterface {
    void addNewItem(MyModel model);
    MyModel getNewsItem(int id);
    List<MyModel> getAllNewsItems();
    int getNewsItemsCount();
    int updateNewsItem(MyModel model);
}
