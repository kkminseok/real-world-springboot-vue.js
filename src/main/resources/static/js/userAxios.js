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

    axios({
        url: "/api/users",
        data: userDTO,
        headers:{ "Content-Type": "application/json"},
        method: "post",
    }).then(function(res){
    alert(res);
    })
}