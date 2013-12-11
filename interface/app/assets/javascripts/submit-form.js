(function ($, window) {
    var SubmitForm;

    SubmitForm = (function () {
        var utils = Utils.getInstance();

        function SubmitForm() {
        }

        var getGrades = function () {
            var studentsPost = {};
            studentsPost.teacher = "";
            studentsPost.clazz = "";
            studentsPost.grades = [];

            var students = $(".student");
            $.each(students, function (i, e) {
                var elem = $(e);
                var name = elem.data("name");
                var grade = elem.find("#" + name + "-grade").text();
                studentsPost.grades.push({name: name, grade: grade});
            });

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
