package pl.edu.agh.iet.to2.teams.api.person;

import pl.edu.agh.iet.to2.teams.api.team.Members;
import pl.edu.agh.iet.to2.teams.api.team.Team;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Created by Pan Ciemnosci on 2015-12-15.
 */
public class TeamManager implements Manager {

    private long id;
    private String name;
    private String occupation;
    private String description;
    private Members subordinates;
    private Manager superior;

    private Set<Team> teams;

    private TeamManager(Person person){
        this.id = person.getId();
        this.name = person.getName() + person.getSurname();
        this.occupation = person.getOccupation();
    }

    public static TeamManager createTeamManager(Person person){
        return new TeamManager(person);
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSurname() {
        return null;
    }

    @Override
    public String getOccupation() {
        return null;
    }

    @Override
    public BigDecimal getSalary() {
        return null;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPosition(String position) {
        this.occupation = occupation;
    }

    public Members getSubordinates() {
        return subordinates;
    }

    public void setSubordinates(Members subordinates) {
        this.subordinates = subordinates;
    }

    public Manager getSuperior() {
        return superior;
    }

    public void setSuperior(Manager superior) {
        this.superior = superior;
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }

    /**
     operations on teams added here
     would be better to move them outside the class
     but "meh" for now
     */

    public void addTeam(Team t){
        teams.add(t);
    }

    public void removeTeam(Team t){
        teams.remove(t);
    }


}
