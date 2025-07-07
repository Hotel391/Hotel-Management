/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import models.RoomImage;
import java.sql.Statement;
import java.sql.Types;

/**
 *
 * @author Hai Long
 */
public class RoomImageDAO {
    private static RoomImageDAO instance;
    private Connection con;

    private RoomImageDAO() {
        con = new DBContext().connect;
    }

    public static RoomImageDAO getInstance() {
        if (instance == null) {
            instance = new RoomImageDAO();
        }
        return instance;
    }
    
    //get all images by typeid
    
    public List<RoomImage> getRoomImagesByTypeId(int typeId) {
        List<RoomImage> list = new ArrayList<>();
        String sql = "SELECT * FROM RoomImage WHERE typeId = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, typeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RoomImage roomImage = new RoomImage();
                    roomImage.setImageId(rs.getInt("RoomImageID"));
                    
                    roomImage.setImage(rs.getString("Image"));
                    list.add(roomImage);
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }
    
    //insert image by typeId no rẹturn imageId
    
    public void insertImage(int typeId, String image) {
        String sql = "INSERT INTO RoomImage (typeId, Image) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, typeId);
            ps.setString(2, image);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    //delete image by imageId
    public void deleteImage(int imageId) {
        String sql = "DELETE FROM RoomImage WHERE RoomImageID = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, imageId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    //update image by imageId
    public void updateImage(int imageId, String image) {
        String sql = "UPDATE RoomImage SET Image = ? WHERE RoomImageID = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, image);
            ps.setInt(2, imageId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    //get image by imageId
    public RoomImage getRoomImageByImageId(int imageId) {
        String sql = "SELECT * FROM RoomImage WHERE RoomImageID = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, imageId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RoomImage roomImage = new RoomImage();
                    roomImage.setImageId(rs.getInt("RoomImageID"));
                    roomImage.setImage(rs.getString("Image"));
                    return roomImage;
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }
    
    public static void main(String[] args) {
        System.out.println(RoomImageDAO.getInstance().getRoomImagesByTypeId(1));
    }
}