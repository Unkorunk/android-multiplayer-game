package ru.timelimit.network;

public class GetMMRResponse extends Response {
    public int MMR;

    public GetMMRResponse() {
        MMR = 100;
    }
    public GetMMRResponse(int MMR) {
        this.MMR = MMR;
    }
}
