package pl.edu.agh.iet.to2.teams.common;

import pl.edu.agh.iet.to2.teams.api.person.*;
import pl.edu.agh.iet.to2.teams.api.team.Team;
import pl.edu.agh.iet.to2.teams.controller.MainController;
import pl.edu.agh.iet.to2.teams.controller.TeamController;
import pl.edu.agh.iet.to2.teams.controller.TeamManagerController;
import pl.edu.agh.iet.to2.teams.controller.TesterPersonController;
import pl.edu.agh.iet.to2.teams.model.TeamsTree;
import pl.edu.agh.iet.to2.teams.view.TeamView;

/**
 * Created by maciek on 21.01.16.
 */
public class TeamsModelManipulator {

    private TeamsTree teamsTree;
    private MainController mainController;
    private TeamView view;
    private TeamData database;

    public TeamsModelManipulator(TeamsTree teamsTree, MainController mainController, TeamView view, TeamData database){
        this.teamsTree = teamsTree;
        this.mainController = mainController;
        this.view = view;
        this.database = database;
    }

    public void setDatabase(TeamData database){
        this.database = database;
    }

    public boolean addTeam(long parentId, Team team)
    // if adding team succeeded, returns true
    // if there is no root in teamsTree, returns false
    // if there is no node with parentId, returns false
    {
        TeamManager parent = teamsTree.findTeamManager(parentId);

        if(teamsTree.rootExists()){
            if(parent == null)
                return false;
            else{
                mainController.addController(TeamController.createControllerOn(team, mainController.getPane(), (TeamView) mainController.getView("TeamView")));

                parent.addTeam(team);

                return true;
            }
        }
        else
            return false;
    }

    public long removeTeam(long id){
        TeamManager teamManager = teamsTree.findManagerOfTeam(id);
        if(teamManager != null)
            for(Team t : teamManager.getTeams())
                if(t.getId() == id){
                    mainController.removeControllerByHashcode(t.hashCode());
                    teamManager.removeTeam(id);
                    return id;
                }

        return -1;
    }

    public long removeTeamByHashcode(int hashcode){
        long id = -1;
        Team res = null;

        TeamManager teamManager = teamsTree.findManagerOfTeamByHashcode(hashcode);
        if(teamManager != null){
            res = teamsTree.findTeamByHashcode(hashcode);
            if(res != null)
                id = res.getId();
            mainController.removeControllerByHashcode(hashcode);
            teamManager.removeTeamByHashcode(hashcode);
            return id;
        }

        return -1;
    }

    public long removeTeam(Team team){
        return this.removeTeamByHashcode(team.hashCode());
    }

    public boolean addTeamManager(long parentId, TeamManager teamManager)
    // parentId = 0 for root
    // if root exists and parentId == 0, returns false
    // if adding team manager succeeded, returns true
    // if there is no node with parentId, returns false
    {
        TeamManager parent = null;

        if(parentId == 0){
            if(teamsTree.rootExists()) {
                return false;
            }
            else{
                mainController.addController(TeamManagerController.createControllerOn(teamManager, mainController.getPane(), (TeamView) mainController.getView("TeamView")));
                teamsTree.setRoot(teamManager);

                view.redrawRoot(teamManager);
                return true;
            }
        }
        else{
            parent = teamsTree.findTeamManager(parentId);
            if(parent == null){
                return false;
            }
            else{
                mainController.addController(TeamManagerController.createControllerOn(teamManager, mainController.getPane(),(TeamView) mainController.getView("TeamView")));
                parent.addManager(teamManager);
                return true;
            }
        }
    }

    public long removeTeamManager(long id){
        if(teamsTree.rootExists() && teamsTree.getRoot().getId() == id){
            mainController.removeControllerByHashcode(teamsTree.getRoot().hashCode());
            teamsTree.deleteRoot();
            return id;
        }

        TeamManager teamManager = teamsTree.findSuperiorOfManager(id);
        if(teamManager != null)
            for(Manager m : teamManager.getManagers())
                if(m.getId() == id){
                    mainController.removeControllerByHashcode(m.hashCode());
                    teamManager.removeManager(id);
                    return id;
                }

        return -1;
    }

    public long removeTeamManagerByHashcode(int hashcode){
        long id = -1;
        TeamManager res = null;

        if(teamsTree.rootExists() && teamsTree.getRoot().hashCode() == hashcode){
            id = teamsTree.getRoot().getId();
            mainController.removeControllerByHashcode(teamsTree.getRoot().hashCode());
            teamsTree.deleteRoot();
            return id;
        }

        TeamManager teamManager = teamsTree.findSuperiorOfManagerByHashcode(hashcode);
        if(teamManager != null){
            res = teamsTree.findTeamManagerByHashcode(hashcode);
            if(res != null)
                id = res.getId();
            mainController.removeControllerByHashcode(hashcode);
            teamManager.removeManagerByHashcode(hashcode);
            return id;
        }

        return -1;
    }

    public long removeTeamManager(TeamManager teamManager){
        return this.removeTeamManagerByHashcode(teamManager.hashCode());
    }


    public boolean addMember(long teamId, TesterPerson testerPerson){
        Team team = teamsTree.findTeam(teamId);
        if(team != null){
            mainController.addController(TesterPersonController.createControllerOn(testerPerson, mainController.getPane(), (TeamView) mainController.getView("TeamView")));
            team.add(testerPerson);
            return true;
        }
        else
            return false;
    }

    public long removeMember(long id){
        Team team = teamsTree.findTeamOfMember(id);
        if(team != null) {
            for (Member m : team.getMembers())
                if (m.getId() == id) {
                    mainController.removeControllerByHashcode(m.hashCode());
                    team.remove(id);
                    return id;
                }
        }

        return -1;
    }

    public long removeMemberByHashcode(int hashcode){
        long id = -1;
        Member res = null;

        Team team = teamsTree.findTeamOfMemberByHashcode(hashcode);
        if(team != null) {
            res = teamsTree.findMemberByHashcode(hashcode);
            if(res != null)
                id = res.getId();
            mainController.removeControllerByHashcode(hashcode);
            team.removeByHashcode(hashcode);
            return id;
        }

        return -1;
    }

    public long removeMember(Member member){
        return this.removeMemberByHashcode(member.hashCode());
    }

}
