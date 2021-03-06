package de.schmitzekater

import grails.validation.Validateable

/**
 * @author Burt Beckwith
 * @author Alexander Schmitz (extended the template from source)
 *
 * Source: http://grails-plugins.github.io/grails-spring-security-core/v3/index.html#personClass
 *
 * Controller for the Domain class User
 */
class UserController {
    static scaffold = User
    static defaultAction = "list"

    def userService             /** Dependency Injection for the UserService    */
    def personService           /** Dependency Injection for the PersonService  */
    def passwordEncoder
    transient springSecurityService

    def index() {
        redirect action: list(), params: params
    }

    def show(){
        redirect action: 'detail', params: params
    }

    /*
    render the view to edit the Password
     */
    def editPassword(){
        log.warn("Deprecated method: editPassword()")
        render view: 'editPassword'  // not used. please verify
    }

    def registerUser(){
        respond new UserRegistrationCommand()
    }
    /*
    Method to lock an User
     */

    def lockAccount(User user) {
        if (userService.lockUser(user)) {
            flash.message = message(code: 'user.accountLocked', args: [user.username])
            log.info(flash.message)
        } else {
            flash.error = message(code: "error.generic.error")
            log.error(flash.message)
        }
        redirect view: '/layouts/list'
    }

    /*
     Method to unlock an User
   */

    def unlockAccount(User user) {
        if (userService.unlockUser(user)) {
            flash.message = message(code: 'user.accountUnLocked', args: [user.username])
            log.info(flash.message)
        } else {
            flash.error = message(code: "error.generic.error")
            log.error(flash.message)
        }
        redirect view: '/layouts/list'
    }

    /*
    Method to enable an User
   */

    def enableAccount(User user) {
        if (userService.enableUser(user)) {
            flash.message = message(code: 'user.accountLocked', args: [user.username])
            log.info(flash.message)
        } else {
            flash.error = message(code: "error.generic.error")
            log.error(flash.message)
        }
        redirect view: '/layouts/list'
    }

    /*
      Method to disable an User
       */

    def disableAccount(User user) {
        if (userService.disableUser(user)) {
            flash.message = message(code: 'user.accountLocked', args: [user.username])
            log.info(flash.message)
        } else {
            flash.error = message(code: "error.generic.error")
            log.error(flash.message)
        }
        redirect view: '/layouts/list'
    }

    /*
     * This function is used when the admin changes the  password for a user.
     * The password will be set to expired, so that the user has to change it upon logon.
     */
    def changeUserPassword(User user) {
        if(user){
            if (params.password.equals(params.newPwRepeat)) {
                def success = userService.changeUserPassword(user, params.password)
                if (success) {
                    flash.message = message(code: 'password.updated.message', args: [user.username])
                    log.info(flash.message)
                    redirect view: '/layouts/list'
                } else {
                    flash.error = message(code: 'error.not.updated.message', args: ['User', user.username])
                    log.error(flash.error)
                    render view: 'editUserPassword', model: [user: user]
                }
            }
            else{
                user.errors.reject('user.rejectPassword.noMatch', 'Password does not match')
                user.errors.rejectValue('password', 'user.rejectPassword.noMatch')
                render view: 'editUserPassword', model: [user: user]
            }
        } else {
            log.error "Error in changeUserPassword for $user.username"
            render view: 'editUserPassword', [user: user]
        }
    }

    /*
    View for the Admin if a password from an user must be edited
     */
    def editUserPassword() {
        def user = User.findById(params.id)
        [user: user]
    }
    /*
     * Render view when user with an expired password try to log on
     */
    def passwordExpired() {
        [username: session['SPRING_SECURITY_LAST_USERNAME']]
    }

    /*
        Render the userLocked view, if an user account is disabled, locked or expired.
     */
    def accountLocked(){
        render view: 'userLocked',  model:[username: session['SPRING_SECURITY_LAST_USERNAME'], userStatus: 'locked']
    }

    def accountDisabled(){
        render view: 'userLocked',  model:[username: session['SPRING_SECURITY_LAST_USERNAME'], userStatus: 'disabled']
    }

    def accountExpired(){
        render view: 'userLocked',model: [username: session['SPRING_SECURITY_LAST_USERNAME'], userStatus: 'expired']
    }

    /*
    Method to save the new password
     */
    def updatePassword(String password, String password_new, String password_new_2) {
        String username = session['SPRING_SECURITY_LAST_USERNAME']
        if(!username) username = params.username  //Workaround
        log.info("User $username")
        if (!username) {
            flash.error = 'Sorry, an error has occurred'
            log.error(flash.error)
            redirect controller: 'login', action: 'auth'
            return
        }
        if (!password || !password_new || !password_new_2 || password_new !=
                password_new_2) {
            flash.error = 'Please enter your current password and a valid new password'
            log.error(flash.error)
            render view: 'passwordExpired', model: [username:
                                                            session['SPRING_SECURITY_LAST_USERNAME']]
            return
        }
        User user = User.findByUsername(username)
        if (!passwordEncoder.isPasswordValid(user.password, password, null /*salt*/)) {
            flash.error = 'Current password is incorrect'
            log.error(flash.error)
            render view: 'passwordExpired', model: [username:
                                                            session['SPRING_SECURITY_LAST_USERNAME']]
            return
        }
        if (passwordEncoder.isPasswordValid(user.password, password_new, null /*salt*/)) {
            flash.error = 'Please choose a different password from your current one'
            log.error(flash.error)
            render view: 'passwordExpired', model: [username:
                                                            session['SPRING_SECURITY_LAST_USERNAME']]
            return
        }
        def success = userService.updatePassword(user, password_new)
        if (success) {
            flash.message = message(code: 'password.updated.message', args: [user.username])
            log.info(flash.message)
            redirect controller: 'login', action: 'auth', model: [username: user.username, password: password_new, user: user]
        } else {
            flash.error = message(code: 'error.not.updated.message', args: ['User', user.username])
            log.error(flash.error)
            redirect action: 'passwordExpired'
        }

    }

    /*
     * Method to create a new User (with existing Person!).
     * @return new User
     */
    def createUser() {
        def person = Person.findById(params.person)
        def user = userService.createUser(params.username, params.password, params.signature, person)
        if(user) {
            def group = RoleGroup.findById(params.userRoleGroup)
            userService.addUserToGroup(user, group)
            flash.message = message(code: 'default.created.message', args: ['User', user.username])
            redirect action: 'detail', id: user.id
        }
        else{
            flash.error = message(code: 'default.not.created.message', args: ['User', user.username])
            respond user.errors, view: 'create'
        }

    }

    /**
     * Method to register a new User and a new Person simultaneously
     * @param urc with params for User and Person
     * @return user
     */
    def register(UserRegistrationCommand urc) {
        if (urc.hasErrors()) {
            render view: "registerUser", model: [user: urc]
            flash.error = message(code: 'form.errors.entries')
        } else {
            //create a new Person instance and save it
            def person = personService.createPerson(urc.lastName, urc.firstName, urc.email)
            // create a new User instance
            def user = userService.createUser(urc.username, urc.password, urc.signature, person)
            if (user) {
                /* Find the group that is selected for the user */
                def roleGroup = RoleGroup.findById(params.userRoleGroup)
                userService.addUserToGroup(user, roleGroup)
                flash.message = message(code: 'default.created.message', args: ['User', user.username])
                log.info(flash.message)
                redirect action: "list"
            } else {
                log.error("User could not be saved")
                redirect view: 'registerUser', userRegistrationCommand: urc
            }
        }
    }

    /*
     * Tabular view of all Users
     */
    def list() {
        if(!params.max) params.max = 10
        def users = User.list(params)
        render view:"/layouts/list", model: [model:users, count: User.count]
    }
    /*
     * Detailed view of one User
     **/
    def detail() {
        def user = User.findById(params.id)
        render view: "/layouts/detail", model: [user: user]
    }

    /*
    Method to save an updated User
     */
    def update(User user) {
        def roleGroup = RoleGroup.findById(params.userRoleGroup)
        if (roleGroup) {
            userService.updateUserGroup(user, roleGroup)
        }
        user.properties = params
        if (user.save()) {
            flash.message = message(code: 'default.updated.message', args: [message(code: 'user.label'), user.username])
            log.info(flash.message)
            redirect(action: "detail", id: params['id'])
        } else {
            flash.error = message(code: 'error.not.updated.message', args: ['User', user.username])
            log.error(flash.error)
            redirect(action: "edit", id: params['id'])
        }
    }



    def handleUserException(UserException ue){
        flash.error = ue.message
        redirect view: '/error', exception: ue
    }

    def handlePersonException(PersonException pe) {
        flash.error = pe.message
        redirect view: '/error', exception: pe
    }

    def handleUserValidationException(UserValidationException uve) {
        log.error(uve.message)
        flash.error = uve.message
        respond uve, [status: 500]
    }
}
/**
 * UserRegistrationCommand
 * Command-Object that stores properties of an User and a Person
 */

class UserRegistrationCommand implements Validateable {
    String username
    String password
    String passwordRepeat
    String signature
    String signatureRepeat
    String firstName
    String lastName
    String email
    Date passwordChangeDate = new Date()

    static constraints = {
        importFrom Person
        importFrom User
        /*
        Password must not be the username!
        Signature must not be the username!
         */
        password  blank: false, nullable: false, validator: { passwd, urc -> return passwd != urc.username }
        passwordRepeat nullable: false, validator: { passwd2, urc ->
            if (passwd2 != urc.password) {
                return 'user.rejectPassword.noMatch'
            } else return (passwd2 == urc.password)
        }
        signature minSize: 6, blank: false, nullable: false, validator: { sig, urc -> return sig != urc.username }
        signatureRepeat nullable: false, blank: false, validator: { sig2, urc ->
            if (sig2 != urc.signature) {
                return 'user.rejectSignature.noMatch'
            }
            return sig2 == urc.signature
        }
    }
}