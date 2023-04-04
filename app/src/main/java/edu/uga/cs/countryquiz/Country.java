package edu.uga.cs.countryquiz;

/**
 * The Country class represents an instance of a Country object.
 *
 * This class can be instantiated by calling the constructor with
 * two params, country and continent.
 *
 * To interact with this class, use the two public methods getCountry() and getContinent().
 */

public class Country {

    private String country;
    private String continent;

    /**
     *
     * @param country a String representation of the country's name
     * @param continent a String representation of the continent name for the Country object
     */
    public Country(String country, String continent) {
        this.country = country;
        this.continent = continent;
    } // Country Constructor

    /**
     * getCountry()
     *
     * We want to use a getter method to interact with getting the object's country name
     * because we're storing the value inside of a private instance variable, with the get method
     * being a public interface for reading the data.
     *
     * We don't want to use a setter method for this value because the data is written one time in
     * the Constructor and should not need to be changed. If a change is required, it would be
     * similarly expensive to just delete the object and rewrite it, simplifying this class's
     * structure.
     *
     * @return a String representation of the country's name
     */
    public String getCountry() {
        return this.country;
    } // getCountry()

    /**
     * getContinent()
     *
     * We want to use a getter method to interact with getting the object's continent name
     * because we're storing the value inside of a private instance variable, with the get method
     * being a public interface for reading the data.
     *
     * We don't want to use a setter method for this value because the data is written one time in
     * the Constructor and should not need to be changed. If a change is required, it would be
     * similarly expensive to just delete the object and rewrite it, simplifying this class's
     * structure.
     *
     * @return a String representation of the continent that the country is on
     */
    public String getContinent() {
        return this.continent;
    } // getContinent()
} // Country Class
