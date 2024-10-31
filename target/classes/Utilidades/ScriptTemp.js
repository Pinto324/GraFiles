let Placeholder = ObjectId('671fbeabbb69b581adfe6912'); 
let resultado = db.archivo.find({
    PropietarioId: Placeholder,
    Habilitado: true
})
printjson(resultado); 