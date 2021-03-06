package tool;

import entity.User;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class GetUserInfor {
    public static User getUserInforByName(String name){
        User user = new User();
        Connection conn = (Connection) DBUtil.getConnection();
        String sql = "select * from user where username = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setHometown(rs.getString("hometown"));
                user.setGender(rs.getString("gender"));
                user.setBirthday(rs.getString("birthday"));
                user.setE_mail(rs.getString("email"));
                user.setPhone_number(rs.getString("phone_number"));
            }
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return user;
    }

    public static int getNewsTypeById(int item_id){
        HashMap<String,Integer> types = new HashMap<>();
        types.put("society",1);
        types.put("sports",2);
        types.put("entertainment",3);
        types.put("science",4);
        types.put("culture",5);
        types.put("film",6);
        types.put("education",7);
        types.put("game",8);
        Connection conn = (Connection) DBUtil.getConnection();
        String sql = "select type from article_train1 where id = ?";
        String type = "";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, item_id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                type = rs.getString("type");
            }
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return types.get(type);
    }

    public static String getUserLikeModeFromDb(int user_id){
        //获取userid的爱好列表
        List<Integer> ls = new ArrayList<>();
        Connection conn = (Connection) DBUtil.getConnection();
        String sql = "select * from article_like where user_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, user_id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                ls.add(rs.getInt("item_id"));
            }
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //根据爱好列表统计各个类别对应的文章数量
        HashMap<Integer,Integer> hm = new HashMap<>();
        hm.put(1,0);
        hm.put(2,0);
        hm.put(3,0);
        hm.put(4,0);
        hm.put(5,0);
        hm.put(6,0);
        hm.put(7,0);
        hm.put(8,0);
        for(int i=0;i<ls.size();i++){
            hm.put(getNewsTypeById(ls.get(i)),hm.get(getNewsTypeById(ls.get(i)))+1);
            System.out.println(getNewsTypeById(ls.get(i)));
        }

        String data = "";
        Iterator iter = hm.entrySet().iterator();
        boolean flag = true;
        while(iter.hasNext()){
            Map.Entry entry = ((Map.Entry) iter.next());
            if(flag){
                data+=entry.getValue();
                flag = false;
            }else{
                data+=","+entry.getValue();
            }
        }
        System.out.println(data);

        return data;
    }

    public static void main(String[] args){

        getUserLikeModeFromDb(0);
    }
}
