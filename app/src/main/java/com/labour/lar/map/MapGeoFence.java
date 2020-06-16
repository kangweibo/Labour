package com.labour.lar.map;

import java.util.List;

public class MapGeoFence {
    public int id;
    public String name;
    public String geotype;
    public int project_id;
    public List<Points> points;


    public class Points {
        public String lon;//": 经度
        public String lat;//": 纬度
    }
}
