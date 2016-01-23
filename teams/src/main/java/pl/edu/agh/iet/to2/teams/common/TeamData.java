package pl.edu.agh.iet.to2.teams.common;

import pl.edu.agh.iet.to2.teams.api.person.Manager;
import pl.edu.agh.iet.to2.teams.api.person.TeamManager;
import pl.edu.agh.iet.to2.teams.api.team.Team;
import pl.edu.agh.iet.to2.teams.db.SqlHelper;
import pl.edu.agh.iet.to2.teams.model.TeamsTree;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TeamData {
    Set<Team> teams;
    Set<DBManager> managers;


    public Set<Team> getAllTeamsFromDb(){
        String query = "SELECT * FROM TEAM";
        List<List> rs = SqlHelper.getResultSet(query, 3);

        Set teams = new HashSet<Team>();

        for(List row : rs){
            Team team = Team.createTeam();
            team.setId((long) row.get(0));
            team.setName(row.get(1).toString());
            team.setProject(null);
            //team.setManager(getManager((long) row.get(2)));
            teams.add(team);
        }
        return teams;
    }


    public class DBManager{
        long id;
        long superiorId;
        // id = person Id, we don't need specific "manager Id"

        public DBManager(long id, long superiodId){
            this.id = id;
            this.superiorId = superiodId;
        }
    }

    public Set<Manager> getAllManagersFromDb(){
        String query = "SELECT * FROM Manager";
        List<List> rs = SqlHelper.getResultSet(query, 3);

        DBManager manager;

        // obtain ids
        for(List row : rs){
            manager = new DBManager((long)row.get(1), //personId
                    (long)row.get(2));//parentManagerId
            managers.add(manager);
        }

        // obtain subordinates/superiors


        return null;
    }

    //all that matters is below
    //you can use methods which are over this, but please continue my tree below and after that delete all trashes
    //i have created not implemented method below to you, so that you can continue it
    //~Iz

    public TeamsTree getTeamsTree() throws Exception {
        String query1 = "SELECT * FROM Manager WHERE parentManagerId=NULL";
        List<List> rs = SqlHelper.getResultSet(query1, 3);
        if(rs.size()==0) return null;
        else if(rs.size()>1) {
            throw new Exception("Check db - there are many roots");
        }
        else {
            TeamsTree teamsTree = new TeamsTree();
            long id = (long)rs.get(0).get(1);
            teamsTree.setRoot(createManager(id));
            return teamsTree;
        }
    }

    private TeamManager createManager(long personId) {
        String getPerson = "SELECT * FROM Person WHERE personId=" + personId;
        List<List> person =  SqlHelper.getResultSet(getPerson, 4);
        TeamManager tm = new TeamManager(personId, person.get(0).get(1).toString(),person.get(0).get(2).toString() );

        long managerId = (long) person.get(0).get(0);

        String getManagerSubordinates = "SELECT * FROM Manager WHERE parentManagerId=" + managerId;
        List<List> managerSubordinates = SqlHelper.getResultSet(getManagerSubordinates, 3);

        for(List manager : managerSubordinates){
            TeamManager managerToAdd = createManager((long) manager.get(1));
            tm.addManager(managerToAdd);
        }

        String getTeamSubordinates = "SELECT * FROM Team WHERE managerId=" + managerId;
        List<List> teamSubordinates = SqlHelper.getResultSet(getTeamSubordinates, 3);

        for(List team : teamSubordinates){
            Team teamToAdd = createTeam((long) team.get(0)); //teamId
            tm.addTeam(teamToAdd);
        }

        return tm;
    }

    private Team createTeam(long l) {
        return null;
        //TODO: not implemented
    }

    //najpierw managerow
    //potem po kolei po nich (przechodzenie po drzewie)
    //i do samego dołu

}