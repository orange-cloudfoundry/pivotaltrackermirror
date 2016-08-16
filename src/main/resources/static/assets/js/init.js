(function ($) {
    $(function () {
        $('select').material_select();
        $('.modal-trigger').leanModal();

        $('#createMirrorModal form button[type=submit]').click(function () {
            var pivotalId = parseInt($('#createMirrorModal #pivotalid').val().replace("https://www.pivotaltracker.com/n/projects/", ""));

            $.ajax({
                dataType: "json",
                url: "/api/mirrorReferences",
                method: "POST",
                contentType: "application/json",
                data: JSON.stringify({
                    "pivotalTrackerProjectId": pivotalId,
                    "target": $('#createMirrorModal #target').val(),
                    "type": $('#createMirrorModal #converter').val(),
                    "token": $('#createMirrorModal #token').val(),
                    "secret": "nothing"
                })
            }).done(function () {
                location.reload();
            }).fail(function (data) {
                $('#errorModal h4').html("Error " + data.responseJSON.status);
                $('#errorModal .error-message').html("<p>" + data.responseJSON.message + "</p>");
                $('#errorModal').openModal();
            });
            return false;
        });
        $('a.delete-mirror').click(function () {
            $.ajax({
                dataType: "json",
                url: $(this).attr('href'),
                method: "DELETE",
                contentType: "application/json"
            }).done(function () {
                location.reload();
            }).fail(function (data) {
                $('#errorModal h4').html("Error " + data.responseJSON.status);
                $('#errorModal .error-message').html("<p>" + data.responseJSON.message + "</p>");
                $('#errorModal').openModal();
            });
            return false;
        });
        $('a.force-update').click(function () {
            $.ajax({
                dataType: "json",
                url: $(this).attr('href'),
                method: "GET",
                contentType: "application/json"
            }).done(function () {
                location.reload();
            }).fail(function (data) {
                $('#errorModal h4').html("Error " + data.responseJSON.status);
                $('#errorModal .error-message').html("<p>" + data.responseJSON.message + "</p>");
                $('#errorModal').openModal();
            });
            return false;
        });
    }); // end of document ready
})(jQuery); // end of jQuery name space