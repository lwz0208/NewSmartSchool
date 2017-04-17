package com.wust.newsmartschool.utils;

import com.wust.newsmartschool.domain.TrainningItem;

import java.util.Comparator;


import java.util.Comparator;

/**
 *
 * @author xiaanming
 *
 */
public class PinyinComparator implements Comparator<TrainningItem> {

    public int compare(TrainningItem o1, TrainningItem o2) {
        if (o1.getSortLetter().equals("@")
                || o2.getSortLetter().equals("#")) {
            return -1;
        } else if (o1.getSortLetter().equals("#")
                || o2.getSortLetter().equals("@")) {
            return 1;
        } else {
            return o1.getSortLetter().compareTo(o2.getSortLetter());
        }
    }

}

