package reservation.dto;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class Response {
    private Object data;
    private String startingThread;
    private String completingThread;
    private long timeMs;
    private boolean error = false;

    public Response() {
        this.startingThread = Thread.currentThread().getName();
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getStartingThread() {
        return startingThread;
    }

    public String getCompletingThread() {
        return completingThread;
    }

    public void setCompletingThread(String completingThread) {
        this.completingThread = completingThread;
    }

    public long getTimeMs() {
        return timeMs;
    }

    public void setTimeMs(long timeMs) {
        this.timeMs = timeMs;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

//    @Override
//    public String toString() {
//        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
//    }
}