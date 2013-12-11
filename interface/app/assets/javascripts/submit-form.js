(function ($, window) {
    var SubmitForm;

    SubmitForm = (function () {
        var utils = Utils.getInstance();

        function SubmitForm() {
        }

        var getGrades = function () {
            var grades = $("#grades");

            var studentsPost = {};
            studentsPost.teacher = grades.data("teacher");
            studentsPost.clazz = grades.data("clazz");
            studentsPost.grades = [];

            var students = $(".student");
            $.each(students, function (i, e) {
                var elem = $(e);
                var id = elem.data("id");
                var username = elem.data("username");
                var grade = elem.find("input").val();
                studentsPost.grades[i] = {};
                studentsPost.grades[i].username = username;
                studentsPost.grades[i].grade = grade;
            });
            debugger;
            return studentsPost;
        };

        SubmitForm.prototype.bindSubmitButton = function () {
            $("#submit-grades").click(function (e) {
                e.preventDefault();

                var url = $("#grades").data("url");
                var variable = getGrades();

                utils.post(url, variable, function (result) {
                    if (result.error) {
                        console.log("Error submiting the grades!");
                    } else {
                        console.log("Grades submited with success!");
                    }
                });
            });
        };

        var instance;
        return {
            getInstance: function () {
                if (instance == null) {
                    instance = new SubmitForm();
                    // Hide the constructor so the returned objected can't be new'd...
                    instance.constructor = null;
                }
                return instance;
            }
        };
    })();

    return window.SubmitForm = SubmitForm;

})(jQuery, window);
