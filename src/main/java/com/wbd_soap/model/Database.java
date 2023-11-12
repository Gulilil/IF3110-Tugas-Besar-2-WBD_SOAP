package com.wbd_soap.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.ibatis.jdbc.ScriptRunner;

import javax.swing.plaf.nimbus.State;

public class Database {
    private Connection connection;
    public Database(){
        try {
            this.connection = DriverManager.getConnection(String.format("jdbc:mysql://host.docker.internal:%s/%s", System.getenv("MYSQL_PORT"), System.getenv("MYSQL_DATABASE")), System.getenv("MYSQL_USER"), System.getenv("MYSQL_PASSWORD"));
            System.out.println("Successfully connect to Database");

            // Only run if needed
//            this.setupDatabase();
        } catch (Exception e){
            System.out.println("Failed connecting to MySQL Database");
        }
    }

    public void setupDatabase(){
        ScriptRunner sr = new ScriptRunner(this.connection);
        try {
            Reader reader;
            reader = new BufferedReader(new FileReader("./src/main/java/com/wbd_soap/db/T01_Logging.sql"));
            sr.runScript(reader);
            reader = new BufferedReader(new FileReader("./src/main/java/com/wbd_soap/db/T02_Reference.sql"));
            sr.runScript(reader);
            System.out.println("Successfully setup database");

        } catch (Exception e){
            System.out.println(e);
            System.out.println("Failed to setup database");
        }
    }

//    public Connection getConnection() {
//        return connection;
//    }

    // ===========
    // Logging
    // ===========
    public void insertLogDatabase(Logging log){
        try {
            Statement stmt = this.connection.createStatement();
            String q = String.format("INSERT INTO logging (description, ip, endpoint) VALUES ('%s', '%s', '%s');",
                    log.getDescription(), log.getIp(), log.getEndpoint());
            stmt.executeUpdate(q);
            System.out.println("Successfully inserting log to Database");
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Failed inserting log to Database");
        }
    }

    // ===========
    // Reference
    // ===========

    public ResultSet getReferenceWithID(int id){
        try {
            Statement stmt = this.connection.createStatement();
            String q = String.format("SELECT * FROM reference WHERE id = %s;", id);
            ResultSet res = stmt.executeQuery(q);
            if (res.next()){
                return res;
            } else {
                return null;
            }
        } catch (Exception e){
            System.out.println("Checking Failed");
            return null;
        }
    }

    public int getReferenceIDWithAnimeID(int anime_account_id){
        try {
            Statement stmt = this.connection.createStatement();
            String q = String.format("SELECT * FROM reference WHERE anime_account_id = %s;", anime_account_id);
            ResultSet res = stmt.executeQuery(q);
            if (res.next()){
                int id = res.getInt("id");
                return id;
            } else {
                System.out.println("There's no such row with anime_account_id: "+ anime_account_id);
                return 0;
            }
        } catch (Exception e){
            System.out.println("Checking Failed");
            return -1;
        }
    }

    public int getReferenceIDWithForumID(int forum_account_id){
        try {
            Statement stmt = this.connection.createStatement();
            String q = String.format("SELECT * FROM reference WHERE forum_account_id = %s;", forum_account_id);
            ResultSet res = stmt.executeQuery(q);
            if (res.next()){
                int id = res.getInt("id");
                return id;
            } else {
                System.out.println("There's no such row with forum_account_id: "+ forum_account_id);
                return 0;
            }
        } catch (Exception e){
            System.out.println("Checking Failed");
            return -1;
        }
    }

    public void insertReferenceDatabase(Reference reference){
        try {
            Statement stmt = this.connection.createStatement();
            String q = String.format("INSERT INTO reference(anime_account_id, forum_account_id, referal_code, point) VALUES (%s,%s,'%s',%s);",
                    reference.getAnimeAccountId(), reference.getForumAccountId() == null? "NULL" : reference.getForumAccountId(), reference.getReferalCode(), reference.getPoint());
            stmt.executeUpdate(q);
            System.out.println("Successfully insert a new Reference data");
        } catch (Exception e){
            System.out.println("Failed inserting reference to Database");
        }

    }

    public void updateReferenceDatabase(Reference reference){
        try {
            Statement stmt = this.connection.createStatement();
            String q = String.format("UPDATE reference SET anime_account_id = %s, forum_account_id = %s, referal_code = '%s', point = %s WHERE id = %s;",
                    reference.getAnimeAccountId(), reference.getForumAccountId() == null? "NULL" : reference.getForumAccountId(), reference.getReferalCode(),reference.getPoint(), reference.getId());
            stmt.executeUpdate(q);
            System.out.println("Successfully update Reference data with id: "+ reference.getId());
        } catch (Exception e){
            System.out.println("Failed updating reference in Database");
        }
    }

    public void deleteReferenceDatabase(int ref_id){
        try {
            Statement stmt = this.connection.createStatement();
            String q = String.format("DELETE FROM reference WHERE id = %s;", ref_id);
            stmt.executeUpdate(q);
            System.out.println("Successfully delete Reference data with id: "+ ref_id);
        } catch (Exception e){
            System.out.println("Failed deleting reference from Database");
        }
    }
}
