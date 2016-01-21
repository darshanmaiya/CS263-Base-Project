echo ""
echo Running command: "curl -X POST --data \"keyname=curl1&value=111\" http://localhost:8080/rest/ds"
echo ""
curl -X POST --data "keyname=curl1&value=111" http://localhost:8080/rest/ds
echo ""
echo Running command: "curl -X POST --data \"keyname=curl1&value=111\" https://cs263-assignment-3.appspot.com/rest/ds"
echo ""
curl -X POST --data "keyname=curl1&value=111" https://cs263-assignment-3.appspot.com/rest/ds

#GET xml (get all):
echo ""
echo Running command: "curl -X GET http://localhost:8080/rest/ds"
echo ""
curl -X GET http://localhost:8080/rest/ds
echo ""
echo Running command: "curl -X GET https://cs263-assignment-3.appspot.com/rest/ds"
echo ""
curl -X GET https://cs263-assignment-3.appspot.com/rest/ds

#GET xml (get individual entity/TaskData (keyname)):
echo ""
echo Running command: "curl -X GET http://localhost:8080/rest/ds/curl1"
echo ""
curl -X GET http://localhost:8080/rest/ds/curl1
echo ""
echo Running command: "curl -X GET https://cs263-assignment-3.appspot.com/rest/ds/curl1"
echo ""
curl -X GET https://cs263-assignment-3.appspot.com/rest/ds/curl1

#GET json (get all):
echo ""
echo Running command: "curl -H \"Accept: application/json\" -X GET http://localhost:8080/rest/ds"
echo ""
curl -H "Accept: application/json" -X GET http://localhost:8080/rest/ds
echo ""
echo Running command: "curl -H \"Accept: application/json\" -X GET https://cs263-assignment-3.appspot.com/rest/ds"
echo ""
curl -H "Accept: application/json" -X GET https://cs263-assignment-3.appspot.com/rest/ds

#GET json (get individual entity/TaskData (keyname)):
echo ""
echo Running command: "curl -H \"Accept: application/json\" -X GET http://localhost:8080/rest/ds/curl1"
echo ""
curl -H "Accept: application/json" -X GET http://localhost:8080/rest/ds/curl1
echo ""
echo Running command: "curl -H \"Accept: application/json\" -X GET https://cs263-assignment-3.appspot.com/rest/ds/curl1"
echo ""
curl -H "Accept: application/json" -X GET https://cs263-assignment-3.appspot.com/rest/ds/curl1

#PUT xml (individual entity/TaskData (keyname):
echo ""
echo Running command: "curl -H \"Content-Type: application/xml\" -X PUT --data "value=112" http://localhost:8080/rest/ds/curl1"
echo ""
curl -H "Content-Type: application/xml" -X PUT --data "value=112" http://localhost:8080/rest/ds/curl1
echo ""
echo Running command: "curl -H \"Content-Type: application/xml\" -X PUT --data "value=112" https://cs263-assignment-3.appspot.com/rest/ds/curl1"
echo ""
curl -H "Content-Type: application/xml" -X PUT --data "value=112" https://cs263-assignment-3.appspot.com/rest/ds/curl1

#POST:
echo ""
echo Running command: "curl -X DELETE http://localhost:8080/rest/ds/curl1"
echo ""
curl -X DELETE http://localhost:8080/rest/ds/curl1
echo ""
echo Running command: "curl -X DELETE https://cs263-assignment-3.appspot.com/rest/ds/curl1"
echo ""
curl -X DELETE https://cs263-assignment-3.appspot.com/rest/ds/curl1

#GET json (get individual entity/TaskData (keyname)):
echo ""
echo Running command: "curl -H \"Accept: application/json\" -X GET http://localhost:8080/rest/ds/curl1"
echo ""
curl -H "Accept: application/json" -X GET http://localhost:8080/rest/ds/curl1
	# should give request failed exception in html (because we just deleted it)
echo ""
echo Running command: "curl -H \"Accept: application/json\" -X GET https://cs263-assignment-3.appspot.com/rest/ds/curl1"
echo ""
curl -H "Accept: application/json" -X GET https://cs263-assignment-3.appspot.com/rest/ds/curl1
	# should give request failed in html (because we just deleted it)
