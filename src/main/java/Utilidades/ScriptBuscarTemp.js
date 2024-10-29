let Placeholder = "Empleado"; 
let resultado = db.archivo.find({
    PropietarioId: Placeholder,
    Habilitado: true
})
printjson(resultado); 