pipeline {
    agent any

    triggers{
        bitbucketPush()
    }
       tools {
        gradle "GradleSetup"
    }


	/* Declare all the variables related to the application
   		Image name : Used for creating docker images 
   		container name : used to run the docker container with given name and image name
   		container prod Name : To distinguish between dev/test env and prod env
   		app port : used to publish the application with the desired port
   		prod app port : To distinguish between dev/test env and prod env
   		version : To maintain the versioning of the releases and docker images with version number
    */
	
 	environment { 
        imageName ='dizitiveit/hrmsapp' 
        containerName='HRMS'
        testContainerName='HRMS-TEST'
        containerPort='8080'
        appPort='9001'
        testAppPort='9002'
        version='1.7'
       credentialsId='80d98917-e8c8-47f4-a529-c94e62614c0f'
        repoURL='github.com/Dizitive-IT-Solutions/project-cpq.git'
        
    }
    
    
    
    
	/* 
	
	This is the starting point of all the pipeline,
	It will execute all the stages one by one.
	
    */
    
    stages {
    
    /* 
	
	This is validation check, this stage is used to for checking valid the version for production.
	As we are considering develop branch is production release version .
	In this step, we are checking already any docker image is present with the given version name, If the same docker image present 
	 we are throwing an error, developer need to change the version and commit the changes.
	
    */
    
     stage ('Validation Check') {
           
            steps {
              echo "current build number: ${currentBuild.number}"   
              echo "current branch name:"+ env.BRANCH_NAME
              echo "current GIT URL:"+ env.GIT_URL

               script {
               		if( env.BRANCH_NAME == 'develop' )
               		{
               			echo "checking docker version present with given version details"
             			def docker_check= sh(script: 'docker images -q ${imageName}:${version} 2> /dev/null', returnStdout: true)
             			echo "checking docker version output "+ (docker_check).length()
             			
             			if((docker_check).length() > 3 )
             			{
             				echo "Image present need to raise error"
             				error("Invalid version number : ${imageName}:${version}")
             			}	
             			
             			
             		}
             	
             	if( env.BRANCH_NAME != 'develop' && env.BRANCH_NAME != 'master' )
               		{
               			
             	 script {
                    dir('git-source-code') {
                          git(
                            url: env.GIT_URL,
                            credentialsId: env.credentialsId
                          )      
                          def tagList = sh(returnStdout: true, script: "git for-each-ref --sort=-taggerdate --format '%(tag)' refs/tags").split()
                          tagList.each { nxtTag ->
                              echo nxtTag
                              if(nxtTag == 'RELEASE-'+env.version)
                              {
                              	echo "Already prod tag present with the same version,raise error"
                              	error("Invalid version number : ${imageName}:${version}")
                              }
                             }
                   		 }
                  		}
             
             			
       		        }
    	        }
        	}
      }
      
      stage('Gradle') {
            steps {
                sh 'gradle --version'
            }
        }
        
   /* 
	This is Build stage, in this step, we are executing gradle build for all the branches except for master.
	sh 'gradle clean build', this will perform the gradle build on the current code
	outcome of this stage is a executable jar file in the build directory
		
    */
        
        stage ('Build & Test Artifact') {
        	 when {
      			 not {
        			   branch 'master'
    		  	 	}
  			 	}
            steps {
              echo "current build number: ${currentBuild.number}"   
			  sh 'gradle clean build'
            }
        }
        
         /* 
		In this stage we are building docker image for all the branches except for master.
		sh 'docker build -t ${imageName}:latest .' --> this will build the docker image, with the image name given on the top of the this file with tag as latesy
		sh 'docker tag ${imageName}:latest ${imageName}:${version}' --> this will tag the latest image to the given version number.
		
    	*/
        
        stage ('Build docker image') {
			when {
      			 not {
        			   branch 'master'
    		  	 	}
  			 	}
    		steps {
        		sh 'docker build -t ${imageName}:latest .'
        		sh 'docker tag ${imageName}:latest ${imageName}:${version}'
    			}
    	}
    	
    	 /* 
		In this stage we are stopping  docker container with the given container name for all the branches except for master.
		sh 'docker stop ${containerName}' -> this will stop the current running container 
		sh 'docker container rm  ${containerName}' -> this will remove the current container from docker
		
    	*/
    	stage ('Stopping Development container') {
    		when {
      			 not {
      			 	 anyOf {
           				 branch 'master'
            	   		 branch 'develop'
				         }
    		  	 	}
  			 	}
    		steps {	
    		script {
            		try {
               			sh 'docker stop ${containerName}'
               			sleep time: 60, unit: 'SECONDS'
        				sh 'docker container rm  ${containerName}'
        				sleep time: 60, unit: 'SECONDS'
           			 } catch (err) {
               			 echo err.getMessage()
           			 }
       			 }
        	
    			}
    	}
    	
    	
    	   	stage ('Stopping production container') {
    			when {
           				 branch 'develop'
  			 		}
    		steps {	
    		script {
            		try {
               			sh 'docker stop ${testContainerName}'
               			sleep time: 60, unit: 'SECONDS'
        				sh 'docker container rm  ${testContainerName}'
        				sleep time: 60, unit: 'SECONDS'
           			 } catch (err) {
               			 echo err.getMessage()
           			 }
       			 }
        	
    			}
    	}
    	
        /* 
		In this stage we are starting docker container with the given container name for all the branches except for master.
		sh 'docker run -d --name ${containerName} -p ${appPort}:${containerPort} ${imageName}:${version}' -> this will run docker container with given name and port
		
    	*/	
    	
    	stage ('starting Development server') {
    		when {
      			 not {
        			   anyOf {
           				 branch 'master'
            	   		 branch 'develop'
				         }
    		  	 	}
  			 	}
  			 	
    		steps {
    		script {
    			sh 'docker run -d --name ${containerName} -p ${appPort}:${containerPort} ${imageName}:${version}'
    		}
        		
    	 }
    	}
    	
    	stage ('starting Production server') {
    		when {
            	   		 branch 'develop'
  			 		}
  			 	
    		steps {
    		script {
    			sh 'docker run -d --name ${testContainerName} -p ${testAppPort}:${containerPort} ${imageName}:${version}'
    		}
        		
    	 }
    	}
    	
    		
        /* 
		In this stage we are tagging the code with release name if the branch is develop only
    	*/
    	
    	stage ('Tag a release') {
    		when {
        			   branch 'develop'
  			 	}
    		steps {
    			    sh  'git config --global user.email "Jenkins@dizitiveti.com"'
 				    sh  'git config --global user.name "Jenkins"'
 				    sh  'git tag -d $(git tag -l)'
    				sh  'git tag -f -a RELEASE-${version} -m "tag a release"'
    				
					
    			withCredentials([[$class: 'UsernamePasswordMultiBinding', 
                credentialsId: env.credentialsId, 
                usernameVariable: 'GIT_USERNAME', 
                passwordVariable: 'GIT_PASSWORD']]) {    
                     script {
                    env.URL_ENCODED_GIT_PASSWORD=URLEncoder.encode(GIT_PASSWORD, "UTF-8")
              		  }
    					sh('git push http://${GIT_USERNAME}:${URL_ENCODED_GIT_PASSWORD}@${repoURL} --tags')
					}
    				    				
    	
    			}
    	}
 	 }
 	 
 	 
}
