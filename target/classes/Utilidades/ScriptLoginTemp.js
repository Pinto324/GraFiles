let usuarioPlaceholder = "empleado"; // Variable que se pasa desde Java
let resultado = db.usuario.findOne({ Username: usuarioPlaceholder });
printjson(resultado); 