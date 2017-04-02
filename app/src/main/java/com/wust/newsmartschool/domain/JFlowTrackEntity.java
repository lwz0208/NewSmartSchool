package com.wust.newsmartschool.domain;

import java.io.Serializable;
import java.util.List;

public class JFlowTrackEntity implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    List<JFlowTrackEntity_Track> Track;

    public List<JFlowTrackEntity_Track> getTrack() {
        return Track;
    }

    public void setTrack(List<JFlowTrackEntity_Track> track) {
        Track = track;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }


}
