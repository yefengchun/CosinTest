package de.schmitzekater

import java.sql.Blob

class System implements Serializable{
    private static final long serialVersionUID = 1
    String systemName
    boolean isActive
    Blob dataFlow
    Date retirementDate // TODO Set it manually
    String area


    static belongsTo = [Person, Department]
    static hasMany = [units: Unit, usesSoftware: Software, systemOwner: Person, processOwner: Person]
    static hasOne = [systemDataCategory: DataCategory, systemDepartment: Department]
    static mappedBy = [ systemOwner: 'systemOwnerSystem', processOwner: 'processOwnerSystem']

    static auditable = true

    static constraints = {
        systemName blank: false, maxSize: 50
        dataFlow nullable: true, display: false
        systemDepartment nullable: true
        systemOwner nullable: true
        processOwner nullable: true
        units nullable: true
        systemDataCategory nullable: true
        area nullable: false, inList: ["GCP", "GLP", "GMP"]
        usesSoftware nullable: true
        retirementDate nullable: true, display: false
    }

    String getDisplayString() {
        return systemName
    }

    String toString() {
        getDisplayString()
    }

    /**
     * Angelehnt an Spring security Plugin (Verbindung System / Computer / Computerrolle)
     */
    Set<Computer> getComputer(){
        SystemRole.findAllBySystem(this)*.computer
    }
    Set<SystemRole> getSystemRole(){
        SystemRole.findAllBySystem(this)
    }

    def onChange = { oldMap, newMap ->
        oldMap.each({ key, oldVal ->
            if (oldVal != newMap[key]) {
                log.info " * $key changed from $oldVal to " + (newMap[key] == null ? 'null' : newMap[key]) + " for " + getDisplayString()
            }
        })

    }
}
