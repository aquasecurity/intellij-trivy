package com.aquasecurity.plugins.trivy.model;

import java.util.Date;

public class ImageConfig {
    public String architecture;
    public Date created;
    public String os;
    public Rootfs rootfs;
    public Config config;
}


