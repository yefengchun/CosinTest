package de.schmitzekater

/**
 * @author Alexander Schmitz
 *
 * Domain class to handle Qualifications.
 * Handled via @QualificationService
 *
 * Qualifications are used for Modules and Software.
 * Different @QualficationTypes can be applied.
 * Example: Calibration for a Module
 * Qualifications can have a document that is stored inside the filesystem in the server.
 */
class Qualification {
    Date qualificationDate
    String comment
    File attachment

    static auditable = true
    static belongsTo = [qualificationObject: QualifiableObject]
    static hasOne = [qualificationType: QualificationType]

    static constraints = {
        attachment nullable: true
        comment nullable: true, maxSize: 1000
        qualificationDate nullable: false, max: new Date()+1 // Qualifications "in the future" are not allowed.
        qualificationType nullable: false
        qualificationObject nullable: true
    }

    /*
    Nice formatted output if used in a String.
    Example: 03-Mar-2015: Validation <qualificationObject>
     */
    String getDisplayString(){
        def text = new StringBuilder()
        text += qualificationDate.format('dd-MMM-yyyy')
        text += ': '
        text += qualificationType.toString()
        text += ' '
        text += qualificationObject.getDisplayString()

        return text
    }

    String toString() {
        getDisplayString()
    }

}
