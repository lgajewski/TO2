package pl.edu.agh.iet.to2.teams.common;

import pl.edu.agh.iet.to2.teams.api.person.TeamManager;
import pl.edu.agh.iet.to2.teams.api.person.TesterPerson;
import pl.edu.agh.iet.to2.teams.api.team.Team;
import pl.edu.agh.iet.to2.teams.db.SqlHelper;
import pl.edu.agh.iet.to2.teams.db.objectsFromDb.DbManagerAccess;
import pl.edu.agh.iet.to2.teams.db.objectsFromDb.DbMemberAccess;
import pl.edu.agh.iet.to2.teams.db.objectsFromDb.DbPersonAccess;
import pl.edu.agh.iet.to2.teams.db.objectsFromDb.DbTeamAccess;
import pl.edu.agh.iet.to2.teams.db.tables.DbManager;
import pl.edu.agh.iet.to2.teams.db.tables.DbMember;
import pl.edu.agh.iet.to2.teams.db.tables.DbTeam;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class TeamData {

    private TeamsModelManipulator teamsModelManipulator;

    public TeamData(TeamsModelManipulator teamsModelManipulator) {
        this.teamsModelManipulator = teamsModelManipulator;
    }


    public boolean getRootManagerFromDb() throws Exception {
        String query1 = "SELECT * FROM Manager WHERE parentManagerPersonId is null";
        List<List> rs = SqlHelper.getResultSet(query1, 3);
        if(rs.size()==0) return false;
        else if(rs.size()>1) {
            throw new Exception("Check db - there are many roots");
        }
        else {
            long id = ((Number)(rs.get(0).get(1))).longValue();
            getTreeBelowManagerFromDb(0, id);
            return true;
        }
    }

    private void getTreeBelowManagerFromDb(long parentId, long personId) {
        String getPerson = "SELECT * FROM Person WHERE personId=" + personId;
        List<List> person =  SqlHelper.getResultSet(getPerson, 4);
        TeamManager tm = new TeamManager(personId, person.get(0).get(1).toString(),person.get(0).get(2).toString() );

        teamsModelManipulator.addTeamManager(parentId, tm);

        String getManagerSubordinates = "SELECT * FROM Manager WHERE parentManagerPersonId=" + personId;
        List<List> managerSubordinates = SqlHelper.getResultSet(getManagerSubordinates, 3);

        for(List manager : managerSubordinates){
            getTreeBelowManagerFromDb(personId, ((Number) manager.get(1)).longValue());
        }

        String getTeamSubordinates = "SELECT * FROM Team WHERE managerPersonId=" + personId;
        List<List> teamSubordinates = SqlHelper.getResultSet(getTeamSubordinates, 3);

        for(List team : teamSubordinates){
            getTeamWithMembersFromDb(personId, ((Number) team.get(0)).longValue());
        }
    }

    private void getTeamWithMembersFromDb(long parentId, long teamId) {
        String getTeam = "SELECT * FROM Team WHERE teamId="+teamId;
        List<List> teamsList = SqlHelper.getResultSet(getTeam, 3);

        Team team = Team.createTeam(teamId);
        team.setName(teamsList.get(0).get(1).toString());

        teamsModelManipulator.addTeam(parentId, team);

        String members = "SELECT * FROM Member WHERE teamId="+teamId;
        List<List> membersList = SqlHelper.getResultSet(members, 3);

        for( List member : membersList){
            getMemberFromDb(teamId, ((Number) member.get(1)).longValue());
        }
    }

    private void getMemberFromDb (long teamId, long personId) {
        String getMember = "SELECT * FROM Person WHERE personId="+personId;
        List<List> members = SqlHelper.getResultSet(getMember, 3);

        TesterPerson testerPerson = new TesterPerson(((Number) members.get(0).get(0)).longValue(), members.get(0).get(1).toString(), members.get(0).get(2).toString());

        teamsModelManipulator.addMember(teamId,testerPerson );
    }

    public long addManagerInDb (String name, String position, long parentManagerId){
        // -1 means that object wasnt added properly into table Person
        // -2 means that object wasnt added properly into table Manager

        long personId = addPersonInDb (name, position);

        if(personId<0) return personId;

        String insertManager;
        if(parentManagerId==0)
            insertManager = "INSERT INTO 'Manager'('personId') VALUES ("+personId+")";
        else
            insertManager = "INSERT INTO 'Manager'('personId','parentManagerPersonId') VALUES ("+personId+", "+parentManagerId+")";

        SqlHelper.executeQuery(insertManager);

        String getManager = "SELECT * FROM Manager WHERE personId="+personId;

        List<List> manager = SqlHelper.getResultSet(getManager, 3);

        if(manager.size()!=0)
            return (long) personId;

        return -2;
    }

    private long addPersonInDb (String name, String position) {
        // -1 means that object wasnt added properly into table Person

        String data = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        String insertPerson = "INSERT INTO 'Person' ( 'name','position', 'added') VALUES ('"+name+"', '"+position+"', "+data+")";
        String getPersonId = "SELECT * FROM Person WHERE name='"+name+"' AND position='"+position+"' AND added="+data;

        SqlHelper.executeQuery(insertPerson);

        List<List> personId = SqlHelper.getResultSet(getPersonId, 4);
        if(personId.size()!=0){
            Object a = personId.get(0).get(0);
            return ((Number) a).longValue();
        }
        return -1;
    }

    public long addEmptyTeam (String name, long managerPersonId){
        //-3 means that object wasnt added properly into table Team
        String addTeam ="INSERT INTO 'Team'( 'name', 'managerPersonId') VALUES ('"+name+"', "+managerPersonId+")";
        String getTeam = "SELECT * FROM Team WHERE name='"+ name+ "' AND managerPersonId="+managerPersonId;

        SqlHelper.executeQuery(addTeam);
        List<List> teams = SqlHelper.getResultSet(getTeam, 3);
        if(teams.size()!=0){
            Object a = teams.get(0).get(0);
            return ((Number) a).longValue();
        }
        return -3;
    }

    public long addMember (String name, String position, long teamId){
        long personId = addPersonInDb (name, position);

        if(personId<0) return personId;

        String addMember ="INSERT INTO 'Member'('personId', 'teamId') VALUES (" + personId + "," + teamId + ")";

        SqlHelper.executeQuery(addMember);

        String getMember = "SELECT * FROM Member WHERE personId="+personId;

        List<List> member = SqlHelper.getResultSet(getMember, 3);

        if(member.size()!=0){
            return personId;
        }
        return -4;
    }

    public void deleteManager(long personId){
        List<DbManager> managers = DbManagerAccess.getManagerByParentManagerPersonId(personId);
        List<DbTeam> teams = DbTeamAccess.getTeamByManagerPersonId(personId);

        for(DbManager manager : managers){
            deleteManager(manager.getPersonId());
        }

        for(DbTeam team : teams){
            deleteTeam(team.getTeamId());
        }

        DbManagerAccess.deleteManagerByPersonId(personId);
        DbPersonAccess.deletePersonByPersonId(personId);
    }

    public void deleteMember(long personId){
        DbMemberAccess.deleteMemberByPersonId(personId);
        DbPersonAccess.deletePersonByPersonId(personId);
    }

    public void deleteTeam (long teamId) {
        List<DbMember> members = DbMemberAccess.getMemberByTeamId(teamId);

        for(DbMember member : members) {
            deleteMember(member.getPersonId());
        }

        DbTeamAccess.deleteTeamByTeamId(teamId);

    }

    public void editMemberName(long personId, String name){
        DbPersonAccess.updatePersonNameByPersonId(personId, name);
    }

    public void editMemberPosition(long personId, String position){
        DbPersonAccess.updatePersonPositionByPersonId(personId, position);
    }

    public void editManagerName(long personId, String name){
        DbPersonAccess.updatePersonNameByPersonId(personId, name);
    }

    public void editManagerPosition(long personId, String position){
        DbPersonAccess.updatePersonPositionByPersonId(personId, position);
    }

    public void editTeamName (long teamId, String name){
        DbTeamAccess.updateTeamNameByTeamId(teamId, name);
    }
}

