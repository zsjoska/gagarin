package ro.gagarin.jdbc.objects;

import ro.gagarin.Person;
import ro.gagarin.PersonTypesEnum;

public class DBPerson implements Person {

    private PersonTypesEnum type;
    private String title;
    private Long id;

    public DBPerson(Long id) {
	this.id = id;
    }

    @Override
    public Long getId() {
	return this.id;
    }

    @Override
    public String getTitle() {
	return this.title;
    }

    @Override
    public PersonTypesEnum getType() {
	return this.type;
    }

}
