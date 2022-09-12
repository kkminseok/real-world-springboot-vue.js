function register() {
    const username = $("#username").val();
    const password = $("#password").val();
    const email = $("#email").val();
    const userDTO = JSON.stringify({
        user: {
            username: username,
            password: password,
            email: email,
        }
    })

    $.ajax({
        url: "/api/users",
        data: userDTO,
        contentType: "application/json",
        async: false,
        type: "POST",
    }).success(function (data) {
        // Todo redirect home login status
        console.log(JSON.stringify(data));
        window.location.href= "/index.html";
        console.log(window.location.href)
    });
}