package com.redli.tmvpsimple.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by redli on 2017/3/31.
 */

public class UserEntity implements Parcelable {

    private String name;
    private int age;

    protected UserEntity(Parcel in) {
        name = in.readString();
        age = in.readInt();
    }

    public static final Creator<UserEntity> CREATOR = new Creator<UserEntity>() {
        @Override
        public UserEntity createFromParcel(Parcel in) {
            return new UserEntity(in);
        }

        @Override
        public UserEntity[] newArray(int size) {
            return new UserEntity[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    public UserEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(age);
    }


}
