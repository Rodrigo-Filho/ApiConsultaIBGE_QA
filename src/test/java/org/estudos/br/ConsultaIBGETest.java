package org.estudos.br;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ConsultaIBGETest {
    private static final String ESTADOS_API_URL = "https://servicodados.ibge.gov.br/api/v1/localidades/estados/";


    @Test
    @DisplayName("Teste distrito simples")
    public void testVerificarDistrito() throws IOException {
        int idDistrito = 355030859; // Distrito que será consultado
        String respostaEsperada = "[{\"id\":355030859,\"nome\":\"Penha\",\"municipio\":{\"id\":3550308,\"nome\":\"São Paulo\",\"microrregiao\":{\"id\":35061,\"nome\":\"São Paulo\",\"mesorregiao\":{\"id\":3515,\"nome\":\"Metropolitana de São Paulo\",\"UF\":{\"id\":35,\"sigla\":\"SP\",\"nome\":\"São Paulo\",\"regiao\":{\"id\":3,\"sigla\":\"SE\",\"nome\":\"Sudeste\"}}}},\"regiao-imediata\":{\"id\":350001,\"nome\":\"São Paulo\",\"regiao-intermediaria\":{\"id\":3501,\"nome\":\"São Paulo\",\"UF\":{\"id\":35,\"sigla\":\"SP\",\"nome\":\"São Paulo\",\"regiao\":{\"id\":3,\"sigla\":\"SE\",\"nome\":\"Sudeste\"}}}}}}]"; // Resposta esperada
        String resposta = ConsultaIBGE.consultarDistrito(idDistrito); // Chama o método utilizado para teste

        // Assert
        assertEquals(respostaEsperada, resposta);
    }

    @Test
    @DisplayName("Pegando o Status Code")
    public void testVerificarEstadoStatusCode() throws IOException {
    
        // Sigla do estado a ser consultado
        String estadoUf = "SP";
        // Cria uma conexão HTTP com a URL da API do IBGE para consultar informações sobre o estado "SP" vindo da variável {estadoUf}
        HttpURLConnection connection = (HttpURLConnection) new URL("https://servicodados.ibge.gov.br/api/v1/localidades/estados/" + estadoUf).openConnection();
        // Define o método da requisição como GET
        connection.setRequestMethod("GET");
        // Obtém o status code da resposta da API
        int statusCode = connection.getResponseCode();
        // Verifica se o status code retornado é igual a 200 (OK) e dá uma msg de erro se não for
        assertEquals(200, statusCode, "O status code da resposta não é 200.");

    }

    // Mock para simular a conexão HTTP
    @Mock
    private HttpURLConnection connectionMock;

    // JSON de resposta simulada para o estado de Minas Gerais (MG)
    private static final String JSON_RESPONSE =
        "{\"id\":31,\"sigla\":\"MG\",\"nome\":\"Minas Gerais\",\"regiao\":{\"id\":3,\"sigla\":\"SE\",\"nome\":\"Sudeste\"}}";

    // Método executado antes de cada teste
    @BeforeEach
    public void setup() throws IOException {
    // Inicializa os mocks
    MockitoAnnotations.openMocks(this);

    // Configura o comportamento do mock
    InputStream inputStream = new ByteArrayInputStream(JSON_RESPONSE.getBytes());
    when(connectionMock.getInputStream()).thenReturn(inputStream);

    }

    @Test
    @DisplayName("Consulta usando o Mock para Estado")
    public void testVerificarComMockEstado() throws IOException {
    // Sigla do estado a ser consultado
    String estadoUf = "MG"; // Minas Gerais

    // Act (Execução do método a ser testado)
    String response =
            ConsultaIBGE.consultarEstado(estadoUf);

    // Verificamos se o JSON retornado é o mesmo que o JSON de resposta simulada
    assertEquals(JSON_RESPONSE, response, "O JSON retornado não corresponde ao esperado.");

    }
}