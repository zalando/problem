package org.zalando.problem;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Nullable;
import java.net.URI;

class MyProblem implements Problem {

    @SerializedName("type")
    private final URI myType;

    @SerializedName("title")
    private final String myTitle;

    @SerializedName("status")
    private final StatusType myStatus;

    @SerializedName("detail")
    private final String myDetail;

    @SerializedName("instance")
    private final URI myInstance;

    private final String misc;

    MyProblem(URI type) {
        this(type, null);
    }

    MyProblem(URI type, @Nullable String title) {
        this(type, title, null);
    }

    MyProblem(URI type, @Nullable String title, @Nullable StatusType status) {
        this(type, title, status, null);
    }

    MyProblem(URI type, @Nullable String title, @Nullable StatusType status,
              @Nullable String detail) {
        this(type, title, status, detail, null);
    }

    MyProblem(URI type, @Nullable String title, @Nullable StatusType status,
              @Nullable String detail, @Nullable URI instance) {
        this(type, title, status, detail, instance, null);
    }

    MyProblem(URI type, @Nullable String title, @Nullable StatusType status,
              @Nullable String detail, @Nullable URI instance, @Nullable String misc) {
        this.myType = type;
        this.myTitle = title;
        this.myStatus = status;
        this.myDetail = detail;
        this.myInstance = instance;
        this.misc = misc;
    }

    @Override
    public URI getType() {
        return myType;
    }

    @Nullable
    @Override
    public String getTitle() {
        return myTitle;
    }

    @Nullable
    @Override
    public StatusType getStatus() {
        return myStatus;
    }

    @Nullable
    @Override
    public String getDetail() {
        return myDetail;
    }

    @Nullable
    @Override
    public URI getInstance() {
        return myInstance;
    }

    @Nullable
    public String getMisc() {
        return misc;
    }
}
