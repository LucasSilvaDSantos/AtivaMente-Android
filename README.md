#  AtivaMente — Gestão de Tarefas e Rotinas

Aplicativo Android nativo focado em produtividade pessoal, organização de rotinas diárias e acompanhamento de progreção, com funcionamento offline e notificações em tempo real.

---

## 📖 Sobre o Projeto

O **AtivaMente** foi desenvolvido com foco em ajudar usuários a estruturarem suas rotinas diárias com lembretes pontuais.
* Arquitetura MVVM (Model-View-ViewModel)
* Persistência Local com Room Database (SQLite)
* Agendamento de tarefas em segundo plano
* Autenticação e nuvem com Firebase
* UI declarativa com Material Design 3 e alternância de temas (Light/Dark Mode)

---

## 🚀 Funcionalidades

O aplicativo oferece uma experiência de organização diária:

* 📝 **Gestão de Tarefas e Rotinas:** Criação, edição e exclusão de tarefas diárias ou de rotinas (com seleção dos dias da semana).
* 🔔 ** Notificações Precisas:** Agendamento em segundo plano que dispara lembretes com hora marcada na barra de notificações.
* 📊 **Gamificação e Métricas:** Acompanhamento de metas diárias em porcentagem, contagem de sequências (*streaks*) e ganho progressivo de XP.
* 🌗 **Temas Dinâmicos:** Alternância fluida e instantânea entre Modo Claro e Modo Escuro nativos.
* 🔒 **Autenticação Segura:** Cadastro de conta, login e logout gerenciados via Firebase Authentication.
* 📅 **Calendário Dinâmico:** Navegação rápida entre os dias da semana para visualização isolada de cada jornada.

---

## 🛠️ Tecnologias Utilizadas

* **Linguagem:** 100 % em Java (Android SDK)
* **Design & UI:** XML Declarativo com Material Design 3 (Material)
* **Arquitetura:** MVVM
* **Persistência de Dados:** Room Database (SQLite)
* **Serviços de Background:** BroadcastReceiver e NotificationManager
* **Segurança:** Firebase Authentication
* **Build System:** Gradle (Kotlin DSL - `.kts`)
* **IDE:** Android Studio
* **Controle de Versão:** Git & GitHub

---

## 📂 Estrutura do Projeto

O código foi organizado seguindo padrões e separação de responsabilidades:

```text
com.example.ativamente/
├── dao/            # Interfaces de acesso ao banco de dados (TaskDao)
├── database/       # Configuração e instância do Room Database (AppDatabase)
├── model/          # Entidades e classes modelo de dados (Task, User)
├── receiver/       # BroadcastReceivers para escuta de alarmes do sistema (Futura)
├── repository/     # Camada de abstração e intermediação de dados
├── view/           # Componentes visuais de UI
│   ├── adapter/    # Adaptadores para Listas e Calendário
│   ├── fragment/   # Fragmentos das telas (Home, Explore, Perfil, Login, etc...)
│   └── dialog/     # Caixas de diálogo
├── viewmodel/      # Camada de gerenciamento de estado e regras de UI
├── MainActivity    # Activity com barra de navegação
└── SplashActivity  # Tela inicial de abertura e verificação de sessão
