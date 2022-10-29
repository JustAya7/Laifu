package com.example.myapplication.dao;

import com.example.myapplication.beans.User;
import com.example.myapplication.utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    /**
     * 添加一个用户
     * @param user 封装了user和password的User对象
     * @return 0为添加失败 1为添加成功
     */
    public int addUser(User user){
        int r = 0;
        //获取连接
        Connection conn = DBUtils.getConnection();
        //sql
        String sql = "INSERT INTO user_group3(id,`user`,`password`)VALUES (NULL,?,?)";
        //预编译
        PreparedStatement ps = null;
        //传参并执行
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,user.getUsername());
            ps.setString(2,user.getPassword());
            r = ps.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            DBUtils.close(null,ps,conn);
        }
        return r;
    }

    /**
     * 更新密码
     * @param user 含有新密码的User对象
     */
    public void updateUser(User user){
        //获取连接
        Connection conn = DBUtils.getConnection();
        //sql
        String sql = "UPDATE user_group3 SET `password`=? WHERE `user`=?";
        //预编译
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,user.getPassword());
            ps.setString(2,user.getUsername());
            ps.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            DBUtils.close(null,ps,conn);
        }
    }

    /**
     * 根据用户名删除用户
     * @param user 用户名
     */
    public void deleteUser(User user){
        //获取连接
        Connection conn = DBUtils.getConnection();
        //sql
        String sql = "DELETE FROM user_group3 WHERE `user`=?";
        //预编译
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,user.getUsername());
            ps.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            DBUtils.close(null,ps,conn);
        }
    }

    /**
     * 根据用户名寻找用户
     * @param user
     * @return
     */
    public User findUserByUser(User user){
        //获取连接
        Connection conn = DBUtils.getConnection();
        //sql
        String sql = "SELECT * FROM user_group3 WHERE `user`=?";
        //预编译
        PreparedStatement ps = null;
        ResultSet rs = null;
        User u = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,user.getUsername());
            rs = ps.executeQuery();
            while(rs.next()){
                u = new User();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("user"));
                u.setPassword(rs.getString("password"));
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            DBUtils.close(rs,ps,conn);
        }
        return u;
    }

    /**
     * 根据用户名密码寻找用户
     * @param user 含有账号密码的User对象
     * @return null则为空
     */
    public User findUserByUserAndPsw(User user) {
        //获取连接
        Connection conn = DBUtils.getConnection();
        //sql
        String sql = "SELECT * FROM user_group3 WHERE `user`=? AND `password`=?";
        //预编译
        PreparedStatement ps = null;
        ResultSet rs = null;
        String password = null;
        User u = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,user.getUsername());
            ps.setString(2,user.getPassword());
            rs = ps.executeQuery();
            while(rs.next()){
                u = new User();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("user"));
                u.setPassword(rs.getString("password"));
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            DBUtils.close(rs,ps,conn);
        }
        return u;
    }


    /**
     * 获取所有数据
     * @return List<User>格式的所有用户数据信息
     */
    public List<User> findAll(){
        //获取连接
        Connection conn = DBUtils.getConnection();
        //sql
        String sql = "SELECT * FROM user_group3";
        //预编译
        PreparedStatement ps = null;
        ResultSet rs = null;
        User u;
        List<User> users = new ArrayList<User>();
        try{
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                u = new User();
                u.setId(rs.getInt(1));
                u.setUsername(rs.getString(2));
                u.setPassword(rs.getString(3));
                users.add(u);
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            DBUtils.close(rs,ps,conn);
        }
        return users;
    }
}