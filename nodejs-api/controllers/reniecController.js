const axios = require('axios');

const URL_DNI = "https://miapi.cloud/v1/dni/";
const URL_RUC = "https://miapi.cloud/v1/ruc/";
const TOKEN_CODE = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjo2MjMsImV4cCI6MTc2NTA4MjQyM30.syuuoYlMvxHXakCtMHYghqsNNBJCEJeaP6VHRa_ZHvY";

const consultarReniec = async (req, res) => {
    const { dni } = req.params;
    let url;

    if (dni.length === 8) {
        url = `${URL_DNI}${dni}`;
    } else if (dni.length === 11) {
        url = `${URL_RUC}${dni}`;
    } else {
        return res.status(400).json({ success: false, message: "DNI o RUC inválido" });
    }

    try {
        const config = { headers: { 'Authorization': `Bearer ${TOKEN_CODE}` } };
        const response = await axios.get(url, config);
        const body = response.data;

        if (!body || body.success !== true) {
            return res.status(404).json({ success: false, message: "No se encontró información" });
        }

        return res.status(200).json(body);

    } catch (error) {
        console.error("Error RENIEC:", error);
        return res.status(500).json({ success: false, message: "Error al consultar API externa" });
    }
};

module.exports = { consultarReniec };