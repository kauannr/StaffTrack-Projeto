# StaffTrack - Gerenciador de funcionários

# Sobre o projeto:

O StaffTrack está em desenvolvimento para uma loja de móveis local. 
O software centraliza informações sobre os funcionários, incluindo metas, realizações, dados contratuais e responsabilidades. 
O sistema permite o cadastro e gerenciamento detalhado de funcionários, com formulários personalizados para cada cargo, garantindo uma interface intuitiva e fácil de navegar para o gestor. Além disso, o sistema gera relatórios completos e individuais para análise de dados e possui uma rotina automatizada de verificação de contratos, enviando alertas por e-mail contendo todos os dado antes dos contratos de expirarem — facilitando ações preventivas inteligentes.

Abaixo há algumas telas da aplicação e suas informações.

# Telas e informações:
## Tela de inicio
![Inicio](https://github.com/kauannr/StaffTrack-Projeto/raw/053a4f889405be332ceaacbff185759413c0677e/assets/Anima%C3%A7%C3%A3o.gif)
  Aqui, após fazer o login com sucesso, podemos escolher ir para a tela de relatórios ou iniciar os cadastros/manutenções/operações. Ao clicar em "Cadastros e Operações" abrirá as opções de cargos para selecionar.

## Relatórios
![Relatorios](https://github.com/kauannr/StaffTrack-Projeto/raw/e3ad7d95fecf4fd9b8d25f52ac9dd2c02d2e2ec6/assets/Captura%20de%20tela%202024-11-05%20001733.png)
  Aqui podemos emitir relatórios tanto geral (de todo o sistema) quanto de um funcionário específico passando o seu identificador. No backend, há a verificagem de qual cargo é este funcionário e assim é emitido o relatório com suas próprias informações.
### Exemplo de relatório: Vendedor
![Relatoriov](https://github.com/kauannr/StaffTrack-Projeto/raw/e3ad7d95fecf4fd9b8d25f52ac9dd2c02d2e2ec6/assets/Captura%20de%20tela%202024-11-05%20002049.png)
![Relatoriov](https://github.com/kauannr/StaffTrack-Projeto/raw/e3ad7d95fecf4fd9b8d25f52ac9dd2c02d2e2ec6/assets/Captura%20de%20tela%202024-11-05%20002118.png)

## Exemplo tela de cadastro: Gerente de vendas
![gerente](https://github.com/kauannr/StaffTrack-Projeto/raw/527b3d6e7fe05b4353cda1a8a6d85d3c5c62ec22/assets/Captura%20de%20tela%202024-10-22%20224618.png)
![gerente](https://github.com/kauannr/StaffTrack-Projeto/raw/527b3d6e7fe05b4353cda1a8a6d85d3c5c62ec22/assets/Captura%20de%20tela%202024-10-22%20224730.png)
![gerente](https://github.com/kauannr/StaffTrack-Projeto/raw/527b3d6e7fe05b4353cda1a8a6d85d3c5c62ec22/assets/Captura%20de%20tela%202024-10-22%20224758.png)
 Aqui é onde acontece a inserção e atualização das informações pessoais e trabalhistas do funcionário. Para não haver repetição de código para todos os caegos, informações genéricas como nome, sobrenome, CPF, etc, são herdadas da classe pai Pessoa. A entidade GerenteVendas no banco de dados representando o cargo armazena apenas os atributos prórprios da entidade. Isso vale para todas.
Apesar de no formulário conter dados do contrato que precisam ser preenchidos ao adicionar um novo funcionário, ao tentar atualizar um funcionário existente esses campos ficam imutáveis impedindo a atualização, pois são informações críticas, que precisam ser atualizadas cuidadosamente e necessitam de uma aba específica para isso. Além disso, são listados com paginação também todos os funcionários deste cargo, assim como também é possivel perquisar por um específico. Cada nome listado possui um link que levará para outra tela contendo mais informações.

## Telas de informação:
### Vendedor
![vendedor](https://github.com/kauannr/StaffTrack-Projeto/raw/527b3d6e7fe05b4353cda1a8a6d85d3c5c62ec22/assets/Captura%20de%20tela%202024-11-04%20235039.png)
 Aqui temos, além dos números e tipo de telefone (residencial, pessoal, etc) temos mais algumas informações sobre o funcionário, como endereço, sexo, salarío e, neste caso, meta de vendas.
Além disso, os todos os botões superiores são customizados. Isso significa que, cada cargo possui opções diferentes, neste caso, há o botão vendas para além de cadastrar vendas, obter todas as informações de vendas do Vendedor. O mesmo não vale para o botão "Contrato" pois está presente em todos os cargos, visto que todo cargo tem um contrato.

### Motorista
![motorista](https://github.com/kauannr/StaffTrack-Projeto/raw/527b3d6e7fe05b4353cda1a8a6d85d3c5c62ec22/assets/Captura%20de%20tela%202024-11-05%20000902.png)

 ### Gerente de vendas
![motorista](https://github.com/kauannr/StaffTrack-Projeto/raw/527b3d6e7fe05b4353cda1a8a6d85d3c5c62ec22/assets/Captura%20de%20tela%202024-11-04%20234656.png)

## Exemplos de telas de contrato e telas próprias:
![contrato](https://github.com/kauannr/StaffTrack-Projeto/raw/527b3d6e7fe05b4353cda1a8a6d85d3c5c62ec22/assets/Captura%20de%20tela%202024-11-04%20234720.png)
![contrato](https://github.com/kauannr/StaffTrack-Projeto/raw/9094bd76b7adbde5fee516ad024731a85a1bed3d/assets/Captura%20de%20tela%202024-10-22%20224952.png)
![contrato](https://github.com/kauannr/StaffTrack-Projeto/raw/9094bd76b7adbde5fee516ad024731a85a1bed3d/assets/Captura%20de%20tela%202024-10-22%20225036.png)
![contrato](https://github.com/kauannr/StaffTrack-Projeto/raw/9094bd76b7adbde5fee516ad024731a85a1bed3d/assets/Captura%20de%20tela%202024-10-22%20225229.png)
 Como digo acima, essas telas com essas funcionalidades estão presentes em todas as entidades. Aqui, podemos visualizar informações do contrato, atualizar o contrato, exibir benefícios ativos e inativos e cadastrar novos benefícios. Note que, quando listados os benefícios, não botão para excluir, pois essa função não deve estar disponível. Apenas podemos mudar o status do benefício, e não exclui-lo da base de daddos, pois mesmo com o beneficio inativo, queremos ele salvo na base para possiveis análises e processos

 ### Informação de vendas - Vendedor:
![vendedor](https://github.com/kauannr/StaffTrack-Projeto/ra2/9094bd76b7adbde5fee516ad024731a85a1bed3d/assets/Captura%20de%20tela%202024-11-04%20235131.png)
![vendedor](https://github.com/kauannr/StaffTrack-Projeto/raw/9094bd76b7adbde5fee516ad024731a85a1bed3d/assets/Captura%20de%20tela%202024-11-04%20235131.png)
 Aqui podemos visualizar todas as vendas feitas por um determinado vendedor e suas referentes informações, além disso, podemos buscar da base de dados uma venda através do seu identificdor ou buscar várias vendas em uma dada faixa de preço. Se quisermos buscar também vendas feitas em um periodo (por exemplo, de 01/01/2024 à 02/02/2025) podemos usar a caixa do "Vendas no periodo". E, lógico, podemos cadastrar uma nova venda também

 ### Entregas do motorista - Motorista:
 ![motorista](https://github.com/kauannr/StaffTrack-Projeto/raw/e5332c028036f6dd5d5ac14d5d364094baf57ce3/assets/Captura%20de%20tela%202024-11-05%20000933.png)
  Aqui encontramos todas as entregas do motorista, cada entrega tem um status, podendo ter sido entregue, cancela ou devolvida. Também temos as informações da entrega como origem, destino e descrição da entrega, e, assim como os benefícios do contrato, não é possivel excluir uma entrega de um motorista. Todas essas informações devem ficar contidas na base de dados

# Email de prevenção:
 ![email](https://github.com/kauannr/StaffTrack-Projeto/raw/e5332c028036f6dd5d5ac14d5d364094baf57ce3/assets/Captura%20de%20tela%202024-11-05%20001959.png)
  Aqui temos o email de prevenção enviado pelo sistema ao próprio manager do sistema, esse email é enviado automaticamente através de uma tarefa agendada no backend onde faz a vereficação continua a cada seis meses. Nele contém todos os contratos com expiração e todas as informações de cada vendedor dono do contrato.

# Observações:
Esta é apenas a primeira versão do projeto, ainda haverá atualizações e implementações. Tudo será devidamente comitado e documentado neste repositório.
A aplicação já se encontra hospedada na AWS, porém, pode estar inativa. Caso haja a curiosidade de ver e testar a funcionalidade da aplicação, basta me mandar um email para que eu a coloque no ar

# Tecnologias utilizadas
## Back end
- Java
- Spring Boot
- JPA / Hibernate
- Junit
- Banco de dados: PostreSQL, AWS RDS
- Hospedagem: AWS EC2
- 
## Front end
- HTML
- CSS
- JavaScript
- Jquey
- Thymeleaf

# Autor
Kauan Ferreira Rodrigues
LinkedIn: https://www.linkedin.com/in/kauan-ferreira-922671240/
Email: kauanfer10@gmail.com
Contato: 83 981527643








