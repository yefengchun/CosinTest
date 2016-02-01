package de.schmitzekater

import grails.transaction.Transactional

/*
    From http://grails-plugins.github.io/grails-spring-security-core/v3/index.html#locking
 */

class ExpirePasswordsJob {
    def sessionRequired = true
    def applicationConfigService
    static triggers = {
      //simple name: 'Password Expire Trigger', repeatInterval: 3000l, startDelay: 60000 , repeatCount: -1// execute job once in 5 Minutes TESTING
        cron name: 'Daily Trigger', cronExpression: '0 5 0 * * ?'  // Daily at 0:05
    }

    @Transactional
    void execute() {
        def date = new Date()
        log.info "Running Job Expire Passwords."


        def users = User.executeQuery(
                'from User u where u.passwordChangeDate <= :cutoffDate',
                [cutoffDate: new Date() - applicationConfigService.passwordExpirationDays]) // TODO: Set real value!!

        for (user in users) {
            // flush each separately so one failure doesn't rollback all of the others
            try {
                if (user.username != 'administrator') {
                    user.passwordExpired = true
                    user.save(flush: true)
                    log.info "Password for $user.username set to expired."
                }
            }
            catch (e) {
                log.error "problem expiring password for user $user.username : $e.message", e
            }
        }
    }
}
