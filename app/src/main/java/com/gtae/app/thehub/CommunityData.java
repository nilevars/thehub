package com.gtae.app.thehub;

/**
 * Created by A E on 29-Jun-17.
 */

public class CommunityData {
    String id;
    String userid;
    String name;
    String admin;
    boolean type;
    boolean searchable;
    CommunityData()
    {

    }
    CommunityData(String id, String name, String admin)
    {
        this.id=id;
        this.name=name;
        this.admin=admin;
    }
    CommunityData( String userid,String name,boolean type,boolean search)
    {
        this.userid=userid;
        this.name=name;
        this.type=type;
        this.searchable=search;
    }

    public String getId() {
        return id;
    }

    public String getAdmin() {
        return admin;
    }

    public String getName() {
        return name;
    }
}
