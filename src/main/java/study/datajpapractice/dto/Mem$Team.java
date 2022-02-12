package study.datajpapractice.dto;

public interface Mem$Team {
    String getUsername();
    TeamInfo getTeam();

    interface TeamInfo {
        String getName();
    }
}
