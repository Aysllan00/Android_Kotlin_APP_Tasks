# Project DM125 - Android Application

Este projeto é uma aplicação Android desenvolvida em Kotlin para gerenciar tarefas, com integração de autenticação via Firebase e notificação. A aplicação permite que os usuários criem, editem, marquem como concluídas e compartilhem tarefas. O projeto também inclui funcionalidades de preferências do usuário, notificações diárias e gerenciamento de permissões.

## Funcionalidades

- **Autenticação com Google**: Os usuários podem se autenticar via Google utilizando Firebase Authentication.
- **Gerenciamento de Tarefas**: Criação, edição, exclusão, marcação como concluída e compartilhamento de tarefas.
- **Notificações Diárias**: A aplicação permite configurar notificações diárias para lembrar o usuário de suas tarefas pendentes.
- **Preferências do Usuário**: Configurações de preferências do usuário para notificações e outros ajustes.
- **Swipe para Excluir**: Use o gesto de swipe para deletar tarefas, com confirmação por diálogo.
- **Offline Capabilities**: Dados de tarefas são armazenados localmente e sincronizados com o Firebase.

## Tecnologias Utilizadas

- **Kotlin**: Linguagem principal de desenvolvimento.
- **Android Jetpack Components**: Utilização de ViewModel, RecyclerView, Data Binding, etc.
- **Firebase Authentication**: Autenticação via Google.
- **Firebase Firestore**: Armazenamento e sincronização de dados das tarefas.
- **Android Notifications**: Notificações diárias.
- **Activity Result API**: Para lidar com permissões e retorno de intenções (intents).

## Server

O backend da aplicação é um servidor local desenvolvido com Spring, que oferece uma API RESTful para o CRUD (Create, Read, Update, Delete) das tarefas.

### Configuração do Servidor

1. Clone o repositório do servidor Spring:

    ```bash
    git clone https://github.com/aduilio/to-do-app-server
    ```

2. Navegue até a pasta do projeto:

    ```bash
    cd to-do-app-server
    ```

3. Compile e execute o servidor local usando o arquivo JAR:

    ```bash
    java -jar bin/application.jar
    ```

4. O servidor estará disponível na URL `http://localhost:8080`, oferecendo as rotas REST para o controle das tarefas.

O servidor pode ser configurado localmente ou em um ambiente de nuvem para disponibilizar as rotas REST usadas no aplicativo Android.

## Pré-requisitos

- Android Studio (versão 4.0 ou superior).
- Conta no Firebase e projeto configurado.
- Conexão com o Firebase Firestore e Firebase Authentication configurada.
- **Servidor Spring** rodando localmente ou em um ambiente remoto.

## Instalação

1. Clone o repositório:

    ```bash
    git clone https://github.com/Aysllan00/Android_Kotlin_APP_Tasks.git
    ```

2. Abra o projeto no Android Studio.

3. Configure o Firebase:
    - Adicione o arquivo `google-services.json` na pasta `app/`.
    - Configure a autenticação no Firebase Authentication (Google Sign-In).
    - Configure o Firestore no Firebase Console.

4. Execute o projeto em um dispositivo físico ou emulador.

5. Certifique-se de que o **servidor Spring** está rodando corretamente.

## Uso

### Login

- O usuário pode se autenticar com sua conta Google. Se não estiver autenticado, a aplicação redireciona automaticamente para a tela de login.

### Gerenciamento de Tarefas

- **Criar Tarefa**: O usuário pode criar uma nova tarefa clicando no botão flutuante (FAB) na tela principal.
- **Editar Tarefa**: Ao clicar em uma tarefa existente, o usuário é redirecionado para uma tela de edição.
- **Excluir Tarefa**: Deslizar a tarefa para a direita exibe um diálogo de confirmação de exclusão.
- **Marcar como Concluída**: Clicando em "Marcar como concluída", a tarefa é movida para a seção de tarefas concluídas.

### Preferências de Notificação

- Na tela de preferências, o usuário pode ativar ou desativar notificações diárias para lembretes de tarefas.

## Estrutura de Pastas

- `activity/`: Contém as atividades principais do aplicativo.
- `adapter/`: Adaptadores para RecyclerViews.
- `entity/`: Contém as classes de modelo, como `Task`.
- `helper/`: Auxiliares para notificações e outras funções utilitárias.
- `service/`: Classes responsáveis pela lógica de negócio, como `TaskService`.
- `listener/`: Interfaces para eventos de clique e swipe nas tarefas.
- `singleton/`: Gerenciamento de preferências e singletons.

## Permissões

A aplicação solicita a permissão de notificações para Android 13+ (Tiramisu):

```xml
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
<uses-permission android:name="android.permission.INTERNET"/>
