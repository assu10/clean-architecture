package com.assu.study.cleanme;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.assu.study.cleanme.archunit.HexagonalArchitecture;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DependencyRuleTest {
  @DisplayName("도메인 레이어는 애플리케이션 레이어로 향하는 의존성이 없음")
  @Test
  void domainLayerDoesNotDependOnApplicationLayer() {
    noClasses()
        .that()
        .resideInAPackage("com.assu.study.cleanme.account.domain..")
        .should()
        .dependOnClassesThat()
        .resideInAnyPackage("com.assu.study.cleanme.account.application..")
        .check(new ClassFileImporter().importPackages("com.assu.study.cleanme"));
  }

  @DisplayName("아키텍처 내의 모든 패키지 간의 의존성 방향 체크")
  @Test
  void validateRegistrationContextArchitecture() {
    // 바운디드 컨텍스트의 부모 패키지 지정 (단일 바운디드 컨텍스트면 애플리케이션 전체에 해당)
    HexagonalArchitecture.boundedContext("com.assu.study.cleanme.account")
        // 도메인 하위 패키지 지정
        .withDomainLayer("domain")

        // 어댑터 하위 패키지 지정
        .withAdaptersLayer("adapter")
        .incoming("in.web")
        .outgoing("out.persistence")
        .and()

        // 애플리케이션 하위 패키지 지정
        .withApplicationLayer("application")
        .services("service")
        .incomingPorts("port.in")
        .outgoingPorts("port.out")
        .and()

        // 설정 하위 패키지 지정
        .withConfiguration("configuration")

        // 의존성 검사
        .check(new ClassFileImporter().importPackages("com.assu.study.cleanme.."));
  }
}
